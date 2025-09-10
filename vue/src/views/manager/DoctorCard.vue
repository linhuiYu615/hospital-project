<template>
  <div>
    <!-- 搜索区域 -->
    <div class="search-bar">
      <el-select
          v-model="departmentId"
          placeholder="请选择科室"
          class="search-select"
      >
        <el-option
            v-for="item in departmentData"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        />
      </el-select>
      <el-button
          type="info"
          plain
          @click="load(1)"
      >
        筛选
      </el-button>
      <el-button
          type="warning"
          plain
          @click="reset"
      >
        重置
      </el-button>
    </div>

    <!-- 列表区域 -->
    <div class="card-container">
      <el-row :gutter="20">
        <el-col
            :span="6"
            v-for="item in tableData"
            :key="item.id"
        >
          <div class="card">
            <img
                :src="item.avatar"
                alt=""
                class="card-avatar"
            />
            <div class="card-header">
              <span class="doctor-name">{{ item.name }}</span>
              <span class="department-name">{{ item.departmentName }}</span>
            </div>
            <div class="card-desc">
              简介：{{ item.description }}
            </div>
            <div class="card-info">
              挂号费:
              <span class="card-price">￥{{ item.price }}</span>
              剩余: {{ item.num }}
            </div>
            <div class="card-footer">
              <el-button
                  type="primary"
                  size="mini"
                  @click="reserve(item.id)"
                  :disabled="item.num === 0"
              >
                挂号
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <div class="pagination-container">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Doctor",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,     // 当前的页码
      pageSize: 10,   // 每页显示的个数
      total: 0,
      departmentId: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      departmentData: []
    }
  },
  created() {
    this.load(1)
    this.loadDepartment()
  },
  methods: {
    reserve(doctorId) {
      if (this.user.role !== 'USER') {
        this.$message.warning('您的角色不支持挂号操作');
        return
      }
      let data = {
        userId: this.user.id,
        doctorId: doctorId
      }
      this.$request.post('/reserve/add', data).then(res => {
        if(res.code === '200') {
          this.$message.success('挂号成功')
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadDepartment() {
      this.$request.get('/department/selectAll').then(res => {
        if(res.code == '200') {
          this.departmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/doctor/selectPage2', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          departmentId: this.departmentId,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.departmentId = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把头像属性换成上传的图片的链接
      this.form.avatar = response.data
    }
  }
}
</script>

<style scoped>
/* 搜索区域样式 */
.search-bar {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  gap: 10px;
  background-color: #fff;
  border-bottom: 1px solid #ebeef5;
}

.search-select {
  width: 200px;
}

/* 卡片容器整体样式 */
.card-container {
  padding: 15px 20px;
}

/* 卡片样式 */
.card {
  margin-top: 20px;
  background-color: #ecf8fd;
  border-radius: 8px;
  padding: 20px;
  height: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

/* 头像 */
.card-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

/* 医生姓名 & 科室 */
.card-header {
  margin-top: 10px;
  font-weight: 550;
  color: #333;
}

.doctor-name {
  margin-right: 5px;
}

.department-name {
  font-weight: 400;
  color: #666;
  font-size: 13px;
}

/* 简介 */
.card-desc {
  margin-top: 10px;
  text-align: left;
  color: #353523;
  line-height: 1.5;
  padding: 0 10px;
  /* 省略号处理 */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4; /* 这里限制显示 4 行 */
}

/* 挂号费信息 */
.card-info {
  margin-top: 10px;
  color: #333;
  font-size: 14px;
}

.card-price {
  color: red;
  font-weight: bold;
  margin-right: 20px;
}

/* 底部按钮 */
.card-footer {
  margin-top: 10px;
}

/* 分页容器 */
.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>