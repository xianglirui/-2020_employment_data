import store from "../store";
import wuhanJson from '../assets/json/wuhan'
import beijingJSon from '../assets/json/beijing'
import shanghaiJSon from '../assets/json/shanghai'
import {wuhan,beijing,shanghai} from '../assets/js/city'
export default function city_index_name(){
  let index = store.state.chooseCity.index
  let map={}
  switch (index) {
    case 0:
      map.json = beijingJSon;
      map.js = beijing
      break;
    case 1:
      map.json = shanghaiJSon;
      map.js = shanghai
      break;
    case 7:
      map.json = wuhanJson;
      map.js = wuhan
      break;
  }
  return map;
}
