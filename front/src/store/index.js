import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
  state:{
    ROBOTCODE:'',
    userInfo:'',
    chooseCity:{name:'武汉',index:7}
  },
  mutations:{
    SET_ROBOTCODE:(state,payload)=>{
      state.ROBOTCODE = payload
    },
    SET_USERINFO:(state,payload)=>{
      state.userInfo = payload
    },
    SET_CHOOSECITY:(state,payload)=>{
      state.chooseCity = payload
    }
  }
})

export default store;
