# 医院预约挂号系统
一个基于 Vue + Spring Boot 的前后端分离医院预约挂号系统

## 技术栈
- **前端**：Vue 2、Vue Router、Axios、Vant
- **后端**：Spring Boot、MyBatis、MySQL
- **开发工具**：IDEA、VSCode、Navicat

## 项目结构
hospital-project/
├── vue/ # 前端项目
├── springboot/ # 后端项目
├── manager.sql # 数据库文件

## 本地启动方式
### 1️⃣ 数据库初始化
1. 打开 MySQL 工具（如 Navicat）
2. 新建数据库（如：`hospital`）
3. 执行根目录下的 `manager.sql`

### 2️⃣ 启动后端（Spring Boot）
1. 用 IDEA 打开 `springboot` 文件夹  
2. 配置数据库连接（`application.yml` 中修改账号密码）  
3. 运行主类启动项目（如：`SpringbootApplication.java`）  
4. 默认端口：`http://localhost:8081`

### 3️⃣ 启动前端（Vue）
1. 用 VSCode 打开 `vue` 文件夹  
2. 安装依赖：
npm install
3. 启动前端：
npm run serve
4. 默认端口：`http://localhost:8080`

<img width="1823" height="904" alt="image" src="https://github.com/user-attachments/assets/624ad63d-06c5-4f4b-8e3b-82a941cdd915" />
<img width="1847" height="890" alt="image" src="https://github.com/user-attachments/assets/b255137f-af4f-44fd-9944-cad4cba8c393" />
<img width="1847" height="902" alt="image" src="https://github.com/user-attachments/assets/ca800847-0d9a-4cb3-b29f-33ee8994da7e" />
<img width="1845" height="909" alt="image" src="https://github.com/user-attachments/assets/3e6d8646-22df-4809-9bfa-76bc7f8a7911" />
