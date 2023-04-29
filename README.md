### Project Description
Reggie take out is a open source project for people learning and gaining a development experience for a modern separation of frontend(VUE js) and backend(Spring boot java) project.

[origin project gitee](https://gitee.com/itxinfei/reggie)

### Changing of the project: 
The origin project is using **Ali Cloud** to implement the SMS login function. Since Amazon Web Services (AWS) is the world's most comprehensive and broadly adopted cloud in 2023, so that I implement the SMS login function on **AWS Cloud**.

### Project Highlight

- Springboot
- Restful API
- mysql
- redis
- mybaits-plus
- AWS SNS service
- Swagger API doc

### Project Setup
#####Insert mysql database and data
```
mysql -u root -p reggie < db_reggie.sql
```
#####Add AWS credentials for SNS service
```
amazonProperties:
    accessKey: {key}
    secretKey: {key}
```

### Role
|Role  | Description | System |
|------------- | ------------- | ------------- |
|Admin  | Manage the employee, food category, setmeal, set price. | Management System|
|Employee  | Employee for Reggie | -|
|User | User of the Reggiee take out application | Reggiee application|

### Learning Date
From 2023-04-14 to 2023-05-01
