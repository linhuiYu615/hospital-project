// request.js
import axios from 'axios'
import router from "@/router";

// 创建一个新的axios实例
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL,   // 后端的接口地址，例如 ip:port
    timeout: 30000                          // 30秒请求超时
})

// 请求拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'; // 设置请求头格式

    const userStr = localStorage.getItem("xm-user")
    let user = {}

    if (userStr && userStr !== 'null') {  // 确保不是字符串 'null'
        try {
            user = JSON.parse(userStr) || {}
        } catch (e) {
            console.error('解析 xm-user 失败:', e)
            user = {}
        }
    }

    config.headers['token'] = user.token || ''  // 设置请求头，确保不为 null

    return config
}, error => {
    console.error('请求错误: ' + error) // 用于调试
    return Promise.reject(error)
});

// 响应拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            try {
                res = res ? JSON.parse(res) : res
            } catch (e) {
                console.error('解析响应数据失败:', e)
            }
        }
        if (res.code === '401') {
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('响应错误: ' + error) // 用于调试
        return Promise.reject(error)
    }
)

export default request
