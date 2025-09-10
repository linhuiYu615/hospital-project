<template>
  <div class="front-page-container">
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>公告：{{ top }}</div>
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">项目前台</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
            <el-menu-item index="/front/home">首页</el-menu-item>
            <el-menu-item index="/front/doctorCard">预约挂号</el-menu-item>
            <el-menu-item index="/front/reserve">我的挂号</el-menu-item>
            <el-menu-item index="/front/record">我的就诊</el-menu-item>
            <el-menu-item index="/front/person">个人中心</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>
    <!--底部固定栏-->
    <div class="footer">
      医院预约挂号系统 ©2024 Created by 虞琳慧
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontLayout",
  data () {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
    }
  },
  mounted() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/front.css";

.front-page-container {
  position: relative;
  min-height: 100vh;
  box-sizing: border-box;
  padding-bottom: 60px; /* 留出底部固定栏的高度 */
}

/* 顶部公告 */
.front-notice {
  background: #fdf6ec;
  padding: 5px 20px;
  border-bottom: 1px solid #f0f0f0;
}

/* 头部区域 */
.front-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-sizing: border-box;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.front-header-left {
  display: flex;
  align-items: center;
}

.front-header-left img {
  height: 40px;
  margin-right: 10px;
}

.front-header-left .title {
  font-size: 18px;
  font-weight: bold;
}

.front-header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.front-header-nav .el-menu {
  display: inline-block;
  vertical-align: middle;
}

.front-header-right {
  display: flex;
  align-items: center;
}

.front-header-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.front-header-dropdown img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

/* 主体内容增加左右留白，让用户体验更佳 */
.main-body {
  margin: 50px 5%;
  box-sizing: border-box;
}

/* 底部固定栏 */
.footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 40px;
  line-height: 40px;
  background: #f0f0f0;
  text-align: center;
  border-top: 1px solid #ccc;
  z-index: 999;
}
</style>
