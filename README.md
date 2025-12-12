# drms-api

[//]: # Implement in this order:()
1) JwtTokenUtil
2) JwtRequestFilter
3) JwtAuthenticationEntryPoint
4) BeanConfig
5) CorsFilterConfiguration
6) SecurityConfig  ← LAST

[//]: # (Script for create table auth_user)
```sql
create table auth_users(
id 	BIGSERIAL primary key,
email Varchar(150) not null,
password varchar(255) not null,
full_name varchar(100) not null,
phone varchar(50),
role varchar(30) not null ,-- 'DISTRIBUTOR' or 'RETAILER'
status varchar(40) not null default 'ACTIVe',
created_at timestamp  not null default 'NOW'
);

