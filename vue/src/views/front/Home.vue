<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <!-- 将轮播图和公告容器包裹在一个父容器中 -->
    <div class="main-container">
      <!--轮播图-->
      <div class="carousel-container">
        <el-carousel height="500px">
          <el-carousel-item v-for="item in bannerImgs" :key="item.id">
            <div class="carousel-item-content">
              <img :src="item.imgUrl" class="carousel-image" alt="轮播图图片"/>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!--公告-->
      <div class="announcement-container">
        <div class="card announcement-card">
          <div class="announcement-title">公告列表</div>
          <div class="announcement-content">
            <el-timeline reverse slot="reference">
              <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
                <el-popover
                    placement="right"
                    width="200"
                    trigger="hover"
                    :content="item.content">
                  <span slot="reference">{{ item.title }}</span>
                </el-popover>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      bannerImgs: []
    }
  },
  created() {
    // 获取公告信息
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
    // 获取轮播图信息
    this.$request.get('/banner/selectAll').then(res => {
      this.bannerImgs = res.data || []
    })
  }
}
</script>

<style scoped>
.carousel-item-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 500px; /* 保持轮播图的固定高度 */
}

.carousel-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

/* 父容器，用于布局轮播图与公告 */
.main-container {
  margin: 10px 0;
}

/* 默认在小屏下堆叠显示 */
.carousel-container,
.announcement-container {
  width: 100%;
}

/* 公告样式 */
.announcement-card {
  box-sizing: border-box;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.announcement-title {
  margin-bottom: 30px;
  font-size: 20px;
  font-weight: bold;
}

.announcement-content {
  flex: 1;
  overflow-y: auto;
  box-sizing: border-box;
  padding-right: 5px; /* 避免滚动条盖住内容 */
}

/* 大屏状态下的布局 */
@media (min-width: 992px) {
  .main-container {
    display: flex;
    flex-direction: row;
    align-items: stretch; /* 子项高度对齐 */
  }

  /* 让轮播图变窄一些，占据部分宽度 */
  .carousel-container {
    width: 60%;
    box-sizing: border-box;
    margin-right: 10px;
  }

  /* 公告在右边，和轮播图等高 */
  .announcement-container {
    flex: 1;
    min-height: 500px; /* 与轮播图等高 */
  }
}
</style>