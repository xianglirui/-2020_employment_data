# coding= utf-8
from selenium import webdriver
from bs4 import BeautifulSoup
from selenium.common.exceptions import StaleElementReferenceException
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
import requests
import time
import sys
from pymongo import MongoClient


def Init():
    # Chrome无界面运行
    chrome_opt = Options()      # 创建参数设置对象.
    chrome_opt.add_argument('--no-sandbox')
    chrome_opt.add_argument('--headless')   # 无界面化.
    chrome_opt.add_argument('--disable-gpu')    # 配合上面的无界面化.
    chrome_opt.add_argument('--window-size=1366,768')   # 设置窗口大小, 窗口大小会有影响.
    options=chrome_opt
    driver=webdriver.Chrome(chrome_options=options)


    # driver=webdriver.Chrome()

    wait = WebDriverWait(driver, 10)

    driver.get("https://www.lagou.com/")

    conn = MongoClient('mongodb://60.205.204.72:27017/')
    db = conn.article
    return driver, wait, db


#选择城市
def click_City(name,driver):
    city = driver.find_element_by_link_text(name)
    city.click()

#输入职业
def job(name,driver):
    search = driver.find_element_by_id("search_input")
    submit = driver.find_element_by_id("search_button")

    search.send_keys(name)
    submit.click()


#依次点开招聘链接
def changePage(key,city,driver,wait,db):
    # 关闭红包弹框
    wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "body-btn"))).click()
    windows=driver.current_window_handle


    # 根据分页总数进行循环：将每页的招聘链接一一点开，然后切换到单页招聘网页窗口，爬取数据，爬完一页就关闭一页窗口
    # 最后切换至信息主页，点击下一页，重复操作
    for i in range(0,6):
        try:
            time.sleep(2)

            # soup = BeautifulSoup(driver.page_source, 'lxml')
            # k = [x.get_text().strip().split('/') for x in soup.find_all(class_='industry')]
            # # 获取融资信息
            # rongzi = [str(x[1]).split() for x in k]
            # # 获取公司规模
            # guimo = [str(x[2]).split() for x in k]

            #获取公司招聘链接列表
            Alist=driver.find_elements_by_tag_name("h3")

            for i in Alist:
                i.click()

            manyPage(key,city,driver,db)

            #切换到最初窗口，点击翻页
            driver.switch_to.window(windows)
            driver.find_element_by_css_selector("#order > li > div.item.page > div.next_disabled.next").click()

        except StaleElementReferenceException:
            #若网络延迟，导致element is not attached to the page document报错，刷新网页，递归执行函数
            driver.refresh()
            changePage(key,city,driver,db)



#爬取招聘单页的信息，并保存到mongodb
def manyPage(key,city,driver,db):
    # 获得打开的第一个窗口句柄
    window_1 = driver.current_window_handle
    windows = driver.window_handles

    # for i in range(21):
    # 切换到最新的窗口
    for current_window in windows:
        if current_window != window_1:
            driver.switch_to.window(current_window)
            html = driver.page_source
            soup = BeautifulSoup(html, 'lxml')
            #获取基础信息：月薪、学历、经验、
            ls = soup.find(class_='job_request').find('h3').find_all('span')

            ls1 = soup.find(class_='work_addr').find_all('a')

            #获取融资阶段
            rongzi = soup.find_all(class_='c_feature_name')[1].text
            #获取公司规模
            guimo = soup.find_all(class_='c_feature_name')[-2].text
            #获取公司地址
            place = {'city': ls1[0].text, 'region': ls1[1].text, 'street': ls1[2].text if ls1[2].text!='查看地图' else None}
            #保存至mongodb
            db.lagouInfo.insert_one({'city': city, 'key': key, 'money': ls[0].text,'grade':ls[3].text[0:-2],'exper':ls[2].text[0:-2],'rongzi':rongzi,'guimo':guimo,'place':place,'require':soup.find(class_='job-detail').text})

            # index += 1 if index < 15 else 0
            #关闭窗口
            driver.close()


# def get_link(key,city):
#     # 关闭红包弹框
#     wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "body-btn"))).click()
#     soup = BeautifulSoup(driver.page_source, 'lxml')
#     #获取职业链接
#     link_list = [x.get('href') for x in soup.find_all(class_='position_link')]
#     print(link_list)
#
#     # s = [x.get_text().strip().split('/') for x in soup.find_all(class_='industry')]
#     k = [x.get_text().strip().split('/')  for x in soup.find_all(class_='industry')]
#     #获取融资信息
#     rongzi = [str(x[1]).split() for x in k]
#     #获取公司规模
#     guimo = [str(x[2]).split() for x in k]
#
#     index = 0
#     for i in link_list:
#         time.sleep(2)
#         soups = BeautifulSoup(requests.get(i,headers={'user-Agent': 'Mozilla/5.0'}).text, 'lxml')
#         #获取基础信息：月薪、学历、经验、
#         ls = soups.find(class_='job_request').find('h3').find_all('span')
#
#         ls1 = soups.find(class_='work_addr').find_all('a')
#         #获取公司地址
#         place = {'city': ls1[0].text, 'region': ls1[1].text, 'street': ls1[2].text if ls1[2].text!='查看地图' else None}
#         #保存至mongodb
#         db.lagouInfo.insert_one({'city': city, 'key': key, 'money': ls[0].text,'grade':ls[3].text[0:-2],'exper':ls[2].text[0:-2],
#                                  'place':place,'rongzi':rongzi[index][0],'guimo':guimo[index][0],'require':soups.find(class_='job-detail').text})
#
#         index+=1


def start(city, work):
    print("调用拉勾爬虫成功！")
    # sys.argv[1]
    driver, wait, db = Init()
    click_City(city, driver)
    job(work, driver)
    try:
        changePage(work, city, driver, wait, db)
        print("success")
    except Exception:
        print("fail")
    print('执行完毕')
    driver.quit()

# if __name__ == '__main__':
#     print("调用拉勾爬虫成功！")
#     #sys.argv[1]
#     driver, wait, db = Init()
#     click_City(sys.argv[1],driver)
#     job(sys.argv[2],driver)
#     try:
#         changePage(sys.argv[2],sys.argv[1],driver,wait,db)
#         print("success")
#     except Exception:
#         print("fail")
#     print('执行完毕')
#     driver.quit()

