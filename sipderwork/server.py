from flask import Flask,request,jsonify
from flask_cors import CORS
import lagouSite
import ten_hot_tech
from concurrent.futures import ThreadPoolExecutor

app = Flask(__name__)

CORS(app)
#线程池
executor = ThreadPoolExecutor(10)

@app.route("/_start",methods=["GET"])
def main():
    # print(request.form)
    # print(request.data)
    # print(request.method)
    # print(request.values)
    # print(request.headers)
    # print(request.args)
    # print(request.json)

    city = request.args.get('city')
    work = request.args.get('work')

    print([city,work])

    # 线程执行，（函数名，多个参数）
    executor.submit(lagouSite.start,city,work)

    return {"status":250}

@app.route("/_tech",methods=["GET"])
def tech():
    city = request.args.get('city')
    key = request.args.get('key')
    flag = request.args.get('flag')

    # 线程执行，（函数名，参数……）
    executor.submit(ten_hot_tech.start, city, key, flag)

    return {"status":250}

if __name__ == '__main__':
    # 开启 debug模式，这样我们就不用每更改一次文件，就重新启动一次服务
    # 设置 host='0.0.0.0'，让操作系统监听所有公网 IP
    # 也就是把自己的电脑作为服务器，可以让别人访问
    app.run(debug=True, host='0.0.0.0',port='8585')