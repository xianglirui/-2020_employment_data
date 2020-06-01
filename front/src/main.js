import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import store from './store'
import router from './router'
import axios from 'axios';
import Vant from 'vant';
import 'vant/lib/index.css';
import {Card,Link, Alert,Button,Message} from 'element-ui';
// import 'prismjs/themes/prism.css';

import VueCookies from 'vue-cookies'
import IconSvg from '@/components/IconSvg/IconSvg'
Vue.component('icon-svg', IconSvg)

Vue.config.productionTip = false
Vue.prototype.$cookies = VueCookies;
Vue.prototype.$message = Message;
Vue.prototype.$axios = axios;
Vue.use(Card);
Vue.use(Link);
Vue.use(Alert);
Vue.use(Button);
Vue.use(VueRouter);
Vue.use(Vuex) ;
Vue.use(VueCookies)
Vue.use(Vant);

new Vue({
  render: h => h(App),
  router,
  store,
}).$mount('#app')
