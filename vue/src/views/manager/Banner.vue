<template>
  <div class="banner-container">
    <!-- 操作按钮区域 -->
    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增轮播图</el-button>
      <el-button type="danger" plain @click="delBatch" class="del-batch-btn">批量删除</el-button>
    </div>

    <!-- 数据表格区域 -->
    <div class="table-wrapper">
      <el-table
          :data="tableData"
          stripe
          @selection-change="handleSelectionChange"
          style="width: 100%;">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
        <el-table-column prop="imgUrl" label="图片" align="center">
          <template v-slot="scope">
            <img :src="scope.row.imgUrl" alt="图片" class="table-image">
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 弹窗表单 -->
    <el-dialog title="轮播图信息" :visible.sync="formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" class="form-content" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="图片" prop="imgUrl">
          <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              name="file"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              :show-file-list="false">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          </el-upload>
          <div v-if="form.imgUrl" class="uploaded-image">
            <img :src="form.imgUrl" alt="图片" class="preview-image">
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Banner",
  data() {
    return {
      tableData: [],        // 表格数据
      pageNum: 1,           // 当前页码
      pageSize: 10,         // 每页显示数量
      total: 0,             // 总条数
      formVisible: false,   // 弹窗显示控制
      form: {
        imgUrl: "",         // 图片URL
      },
      rules: {
        imgUrl: [
          {required: true, message: '请上传图片', trigger: 'blur'},
        ],
      },
      ids: [],               // 选中的ID集合
      uploadUrl: 'http://localhost:9090/files/upload', // 使用完整的上传 URL
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    // 新增轮播图
    handleAdd() {
      this.form = { imgUrl: "" }  // 清空表单数据
      this.formVisible = true
    },
    // 编辑轮播图
    handleEdit(row) {
      this.form = { ...row }  // 复制选中的行数据
      this.formVisible = true
    },
    // 保存新增或编辑的轮播图
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          console.log('Form data before save:', this.form) // 调试日志
          const apiUrl = this.form.id ? '/banner/update' : '/banner/add'
          const method = this.form.id ? 'PUT' : 'POST'
          this.$request({
            url: apiUrl,
            method: method,
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)  // 刷新表格数据
              this.formVisible = false
            } else {
              this.$message.error(res.msg)
            }
          }).catch(err => {
            console.error(err)
            this.$message.error('保存失败')
          })
        }
      })
    },
    // 删除单个轮播图
    del(id) {
      this.$confirm('您确定删除这条轮播图吗？', '确认删除', {type: "warning"})
          .then(() => {
            this.$request.delete(`/banner/delete/${id}`).then(res => {
              if (res.code === '200') {
                this.$message.success('删除成功')
                this.load(1)
              } else {
                this.$message.error(res.msg)
              }
            }).catch(err => {
              console.error(err)
              this.$message.error('删除失败')
            })
          })
          .catch(() => { /* 取消操作 */
          })
    },
    // 批量删除轮播图
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的轮播图')
        return
      }
      this.$confirm('您确定批量删除选中的轮播图吗？', '确认删除', {type: "warning"})
          .then(() => {
            this.$request.delete('/banner/delete/batch', {data: this.ids}).then(res => {
              if (res.code === '200') {
                this.$message.success('批量删除成功')
                this.load(1)
              } else {
                this.$message.error(res.msg)
              }
            }).catch(err => {
              console.error(err)
              this.$message.error('批量删除失败')
            })
          })
          .catch(() => { /* 取消操作 */
          })
    },
    // 处理表格选择变化
    handleSelectionChange(rows) {
      this.ids = rows.map(row => row.id)
    },
    // 分页查询
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/banner/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
        } else {
          this.$message.error(res.msg)
        }
      }).catch(err => {
        console.error(err)
        this.$message.error('加载数据失败')
      })
    },
    // 页码变化处理
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    // 上传图片成功处理
    handleUploadSuccess(response, file, fileList) {
      console.log('Upload response:', response) // 调试日志
      if (response.code === '200') {
        // 假设 response.data 包含上传后的图片URL
        this.form.imgUrl = response.data.url || encodeURI(response.data)  // 根据后端返回的数据结构调整
        console.log('Updated form.imgUrl:', this.form.imgUrl) // 确认赋值
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.msg)
      }
    },
    // 上传失败处理
    handleUploadError(error, file, fileList) {
      console.error('Upload error:', error) // 调试日志
      this.$message.error('上传失败，请重试')
    },
    // 上传前验证
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        this.$message.error('只能上传图片文件')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    }
  }
}
</script>

<style scoped>
.banner-container {
  padding: 20px;
  box-sizing: border-box;
  width: 100%;
}

.operation {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.del-batch-btn {
  margin-left: 10px;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto; /* 处理小屏幕下的横向滚动 */
}

.el-table {
  width: 100%;
}

.el-table-column {
  /* 移除固定宽度，让列自适应 */
}

.table-image {
  width: 100px;
  height: auto;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.upload-demo i {
  font-size: 28px;
  color: #1890ff;
}

.upload-demo .el-upload__text {
  margin-top: 10px;
  font-size: 14px;
}

.uploaded-image {
  margin-top: 10px;
}

.preview-image {
  width: 100px;
  height: auto;
}

.form-content {
  padding-right: 50px;
}

.dialog-footer {
  text-align: right;
}
</style>