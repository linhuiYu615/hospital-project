<template>
  <div>
    <!-- 搜索区域 -->
    <div class="search">
      <el-input
          placeholder="请输入患者姓名查询"
          style="width: 200px"
          v-model="userName"
      ></el-input>
      <el-button
          type="info"
          plain
          style="margin-left: 10px"
          @click="load(1)"
      >查询</el-button
      >
      <el-button
          type="warning"
          plain
          style="margin-left: 10px"
          @click="reset"
      >重置</el-button
      >
    </div>

    <!-- 表格区域 -->
    <div class="table">
      <el-table
          :data="tableData"
          stripe
          @selection-change="handleSelectionChange"
      >
        <el-table-column
            prop="id"
            label="序号"
            width="80"
            align="center"
            sortable
        ></el-table-column>
        <el-table-column
            prop="userName"
            label="患者姓名"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column
            prop="doctorName"
            label="医生姓名"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="time" label="就诊时间"></el-table-column>
        <el-table-column prop="medicalRecord" label="医嘱病例">
          <template v-slot="scope">
            <el-button
                type="primary"
                size="mini"
                @click="viewEditor(scope.row.medicalRecord)"
            >查看病例</el-button
            >
          </template>
        </el-table-column>
        <el-table-column prop="inhospital" label="是否住院"></el-table-column>
        <el-table-column
            prop="inhospitalRecord"
            label="是否住院登记"
        ></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button
                plain
                type="primary"
                v-if="user.role === 'DOCTOR'"
                @click="handleEdit(scope.row)"
                size="mini"
            >填写医嘱病例</el-button>
            <el-button
                plain
                type="primary"
                v-if="user.role === 'ADMIN' && scope.row.inhospital === '是' && scope.row.inhospitalRecord === '否'"
                @click="registration(scope.row)"
                size="mini"
            >住院登记</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <!-- 医嘱病例填写弹窗 -->
    <el-dialog
        title="医嘱病例填写"
        :visible.sync="fromVisible"
        width="60%"
        :close-on-click-modal="false"
        destroy-on-close
        @opened="dialogOpened"
    >
      <el-form
          label-width="100px"
          style="padding-right: 50px"
          :model="form"
          :rules="rules"
          ref="formRef"
      >
        <el-form-item prop="medicalRecord" label="医嘱病例">
          <div id="editor" ></div>
        </el-form-item>
        <el-form-item prop="inhospital" label="是否住院">
          <el-select v-model="form.inhospital" placeholder="请选择" style="width: 100%">
            <el-option label="是" value="是"></el-option>
            <el-option label="否" value="否"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 医嘱病例查看弹窗 -->
    <el-dialog
        title="医嘱病例"
        :visible.sync="editorVisible"
        width="50%"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <div v-html="viewContent" class = "w-e-text"></div>
    </el-dialog>
  </div>
</template>

<script>
import WangEditor from 'wangeditor';
import registration from "@/views/manager/Registration.vue";

export default {
  name: 'Record',

  data() {
    return {
      tableData: [], // 表格数据
      pageNum: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      total: 0, // 数据总数
      userName: null, // 查询标题
      fromVisible: false, // 表单弹窗显示控制
      form: {}, // 表单数据
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'), // 用户信息
      rules: {
        inhospital:[{ required: true, message: '请选择是否住院', trigger: 'blur' }],
      },
      ids: [], // 选中的数据ID集合
      viewContent: null, // 查看内容
      editor: null, // 编辑器实例
      editorVisible: false, // 查看弹窗显示控制
    };
  },
  created() {
    this.load(1);
  },
  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy();
      this.editor = null;
    }
  },
  methods: {
    registration(row){
      let data = JSON.parse(JSON.stringify(row))
      data.inhospitalRecord = '是'
      this.$request.put('/record/update',data).then(res =>{
        if (res.code === '200'){
          this.load(1)
          this.toRegistration(row.userId)
        }
      })
    },
    toRegistration(userId){
      let data = {
        userId:userId,
        hosStatus:'住院中'
      }
      this.$request.post('/registration/add',data).then(res =>{
        if (res.code === '200'){
          this.$message.success('登记成功')
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    // 弹窗打开后初始化编辑器
    dialogOpened() {
      this.initWangEditor(this.form.medicalRecord || '');
    },
    // 查看医嘱病例
    viewEditor(content) {
      this.viewContent = content;
      this.editorVisible = true;
    },
    // 新增医嘱病例
    handleAdd() {
      this.form = {};
      this.fromVisible = true;
      // 如果需要在弹窗打开后立即初始化编辑器，可以使用 $nextTick
      // this.$nextTick(() => {
      //   this.initWangEditor('');
      // });
    },
    // 编辑医嘱病例
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.fromVisible = true;
      // this.$nextTick(() => {
      //   this.initWangEditor(this.form.medicalRecord || '');
      // });
    },
    // 初始化编辑器
    initWangEditor(content) {
      if (this.editor) {
        this.editor.destroy();
      }

      const E = WangEditor;
      this.editor = new E('#editor');
      this.editor.config.placeholder = '请输入内容';
      this.editor.config.uploadFileName = 'file';
      this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload';

      // 如需其他配置，可在此添加

      this.editor.create();
      this.editor.txt.html(content);
    },
    // 保存医嘱病例
    save() {
      this.form.medicalRecord = this.editor.txt.html();
      this.$request.put('/record/update', this.form).then((res) => {
        if (res.code === '200') {
          this.$message.success('保存成功');
          this.load(1);
          this.fromVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 删除单条记录
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', { type: 'warning' })
          .then(() => {
            this.$request.delete('/record/delete/' + id).then((res) => {
              if (res.code === '200') {
                this.$message.success('操作成功');
                this.load(1);
              } else {
                this.$message.error(res.msg);
              }
            });
          })
          .catch(() => {});
    },
    // 处理表格选择变化
    handleSelectionChange(rows) {
      this.ids = rows.map((v) => v.id);
    },
    // 批量删除
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据');
        return;
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', { type: 'warning' })
          .then(() => {
            this.$request.delete('/record/delete/batch', { data: this.ids }).then((res) => {
              if (res.code === '200') {
                this.$message.success('操作成功');
                this.load(1);
              } else {
                this.$message.error(res.msg);
              }
            });
          })
          .catch(() => {});
    },
    // 加载数据
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request
          .get('/record/selectPage', {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              userName: this.userName,
            },
          })
          .then((res) => {
            this.tableData = res.data?.list;
            this.total = res.data?.total;
          });
    },
    // 重置查询
    reset() {
      this.userName = null;
      this.load(1);
    },
    // 分页变化
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
  },
};
</script>
<style scoped>
/* 根据需要添加样式 */
</style>
