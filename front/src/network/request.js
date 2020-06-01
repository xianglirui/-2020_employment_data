import axios from 'axios'
import store from '../store'
import cookie from 'js-cookie'
import router from "../router";
import {generateUUID} from "../utils/utils";
import compare from "../utils/compare";


// 创建axios实例
const service = axios.create({
  baseURL: 'http://127.0.0.1:8181',
  timeout: 25000
})

//request拦截器
service.interceptors.request.use(config => {
  if(cookie.get('token')){
    config.headers['Token'] = cookie.get('token')
    config.headers['ROBOTCODE'] = cookie.get('ROBOTCODE')
    config.headers['city'] = Object.keys(compare()).length!==0?store.state.chooseCity.index:7
  }
  return config
}, error => {
  return Promise.reject(error)
})

//response拦截器
service.interceptors.response.use(response => {
  const res = response.data;
  if(res.code===4001 || res.code===4002){
    router.push({path: '/login'}).then(r => {})
    return response
  }else {
    return response
  }
},error => {})



export function getInfo(code) {
  return service({
    url: '/getUserInfo',
    method: 'get',
    params:{code}
  });
}

export function getRandom() {
  return service({
    url: '/getRandom',
    method: 'get',
  });
}

export function ok() {
  return service({
    url:'/ok',
    method:'get'
  })
}

export function totalData(choice) {
  return service({
    url:'totalData',
    method:'post',
    data:choice
  })
}

export function returnArea(key) {
  return service({
    url:'returnArea',
    method:'get',
    params:{key}
  })
}

export function returnHotTech(key) {
  return service({
    url:'returnHotTech',
    method:'get',
    params:{key}
  })
}

export function noSuit(key,err) {
  return service({
    url:'noSuitable',
    method:'get',
    params:{key,err}
  })
}

export function returnTrend(key) {
  return service({
    url:'returnTrend',
    method:'get',
    params:{key}
  })
}

export function search(key) {
  return service({
    url:'onsearch',
    method:'get',
    params:{key}
  })
}



