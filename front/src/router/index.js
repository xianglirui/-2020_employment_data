import Vue from 'vue'
import Router from 'vue-router'
import store from "../store";
import cookie from 'js-cookie'
import {getInfo} from "../network/request";
import {generateUUID} from "../utils/utils";
import {Dialog} from "vant";

Vue.use(Router)

const route=[
  {
    path: '',
    redirect: '/cover'
  },
  {
    path:'/cover',
    component:()=>import('@/components/cover/Cover')
  },
  {
    path:'/login',
    component:()=>import('@/components/Login/Login')
  },
  {
    path:'/home',
    component:()=>import('@/components/Home/Home')
  },
  {
    path:'/pc_err',
    component:()=>import('@/components/cover/PC')
  }
]

const asynRoute=[
  {
    path:'/docs',
    component:()=>import('@/components/document/Document')
  },
]


const router = new Router({
  mode:'history',
  routes:route
})


let i = 1;
const whiteList=['/login','/docs','/home','/cover','/pc_err']



router.beforeEach((to,from,next)=>{
  // if(to.path==='/home' && from.path==='/login'){
  //   //只要登录就会在cookie里存一个ROBOTCODE，值为uuid，存活时间一小时
  //   let code = generateUUID();
  //   cookie.set('ROBOTCODE',code,60*60);
  //   //跳转到主页面之前，都会去验证token（后台是根据uuid生成令牌），
  //   // 验证通过返回状态码200，若状态码为其他，直接转回login页面
  //   getInfo(code).then(res=>{
  //     cookie.set('token',res.data,60*60)
  //     next()
  //   })
  // }

  if(to.path==='/docs' ){
    if(from.path==='/home') {
      if (i !== 0) {
        router.options.routes = route.concat(asynRoute)
        router.addRoutes(asynRoute)
        i = 0
        next(to)
      }
    }else {
      cookie.remove('token')
      cookie.remove('ROBOTCODE')
      next('/login')
    }
  }

  if(whiteList.indexOf(to.path)!==-1){
    next()
  }else {
    next('/login')
  }


  next()

})

export default router
