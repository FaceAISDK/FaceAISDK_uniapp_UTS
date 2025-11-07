import App from './App.uvue'

import { createSSRApp } from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
export function main(app: IApp) {
    definePageRoutes();
    defineAppConfig();
    (createApp()['app'] as VueApp).mount(app, GenUniApp());
}

export class UniAppConfig extends io.dcloud.uniapp.appframe.AppConfig {
    override name: string = "人脸识别_UTS"
    override appid: string = "__UNI__A6AD04B"
    override versionName: string = "2025.11.01"
    override versionCode: string = "20251101"
    override uniCompilerVersion: string = "4.76"
    
    constructor() { super() }
}

import GenPagesIndexIndexClass from './pages/index/index.uvue'
function definePageRoutes() {
__uniRoutes.push({ path: "pages/index/index", component: GenPagesIndexIndexClass, meta: { isQuit: true } as UniPageMeta, style: _uM([["navigationBarTitleText","FaceAISDK Demo演示"]]) } as UniPageRoute)
}
const __uniTabBar: Map<string, any | null> | null = null
const __uniLaunchPage: Map<string, any | null> = _uM([["url","pages/index/index"],["style",_uM([["navigationBarTitleText","FaceAISDK Demo演示"]])]])
function defineAppConfig(){
  __uniConfig.entryPagePath = '/pages/index/index'
  __uniConfig.globalStyle = _uM([["navigationBarTextStyle","black"],["navigationBarTitleText","uni-app x"],["navigationBarBackgroundColor","#F8F8F8"],["backgroundColor","#F8F8F8"]])
  __uniConfig.getTabBarConfig = ():Map<string, any> | null =>  null
  __uniConfig.tabBar = __uniConfig.getTabBarConfig()
  __uniConfig.conditionUrl = ''
  __uniConfig.uniIdRouter = _uM()
  
  __uniConfig.ready = true
}
