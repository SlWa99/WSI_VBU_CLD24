# Database migration

In this task you will migrate the Drupal database to the new RDS database instance.

![Schema](./img/CLD_AWS_INFA.PNG)

## Task 01 - Securing current Drupal data

### [Get Bitnami MariaDb user's password](https://docs.bitnami.com/aws/faq/get-started/find-credentials/)

```bash
[INPUT]
//help : path /home/bitnami/bitnami_credentials
bitnami@ip-10-0-7-10:~$ cat /home/bitnami/bitnami_credentials

[OUTPUT]
Welcome to the Bitnami package for Drupal

******************************************************************************
The default username and password is 'user' and 'vec2PoZB3aQ:'.
******************************************************************************

You can also use this password to access the databases and any other component the stack includes.

Please refer to https://docs.bitnami.com/ for more details.
```

### Get Database Name of Drupal

```bash
[INPUT]
//add string connection
/opt/bitnami/mariadb/bin/mariadb -u bn_drupal -p
password > 2b9defd18a354804a1d4c4742c252fb39d808c12cfc2046ffc8f31432ae8a060
MariaDB [(none)]> show databases;

[OUTPUT]
+--------------------+
| Database           |
+--------------------+
| bitnami_drupal     |
| information_schema |
+--------------------+
2 rows in set (0.001 sec)

[INPUT]
/opt/bitnami/mariadb/bin/mariadb -u root -p
Password > vec2PoZB3aQ:

[OUTPUT]
+--------------------+
| Database           |
+--------------------+
| bitnami_drupal     |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
| test               |
+--------------------+

[INPUT]
MariaDB [(none)]> use bitnami_drupal;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
MariaDB [bitnami_drupal]> show tables;

[OUTPUT]
    `> +----------------------------------+
    `> | Tables_in_bitnami_drupal         |
    `> +----------------------------------+
    `> | block_content                    |
    `> | block_content__body              |
    `> | block_content_field_data         |
    `> | block_content_field_revision     |
    `> | block_content_revision           |
    `> | block_content_revision__body     |
    `> | cache_bootstrap                  |
    `> | cache_config                     |
    `> | cache_container                  |
    `> | cache_data                       |
    `> | cache_default                    |
    `> | cache_discovery                  |
    `> | cache_dynamic_page_cache         |
    `> | cache_entity                     |
    `> | cache_menu                       |
    `> | cache_page                       |
    `> | cache_render                     |
    `> | cache_toolbar                    |
    `> | cachetags                        |
    `> | comment                          |
    `> | comment__comment_body            |
    `> | comment_entity_statistics        |
    `> | comment_field_data               |
    `> | config                           |
    `> | file_managed                     |
    `> | file_usage                       |
    `> | flood                            |
    `> | help_search_items                |
    `> | history                          |
    `> | key_value                        |
    `> | key_value_expire                 |
    `> | menu_link_content                |
    `> | menu_link_content_data           |
    `> | menu_link_content_field_revision |
    `> | menu_link_content_revision       |
    `> | menu_tree                        |
    `> | node                             |
    `> | node__body                       |
    `> | node__comment                    |
    `> | node__field_image                |
    `> | node__field_tags                 |
    `> | node_access                      |
    `> | node_field_data                  |
    `> | node_field_revision              |
    `> | node_revision                    |
    `> | node_revision__body              |
    `> | node_revision__comment           |
    `> | node_revision__field_image       |
    `> | node_revision__field_tags        |
    `> | path_alias                       |
    `> | path_alias_revision              |
    `> | queue                            |
    `> | router                           |
    `> | search_dataset                   |
    `> | search_index                     |
    `> | search_total                     |
    `> | semaphore                        |
    `> | sequences                        |
    `> | sessions                         |
    `> | shortcut                         |
    `> | shortcut_field_data              |
    `> | shortcut_set_users               |
    `> | taxonomy_index                   |
    `> | taxonomy_term__parent            |
    `> | taxonomy_term_data               |
    `> | taxonomy_term_field_data         |
    `> | taxonomy_term_field_revision     |
    `> | taxonomy_term_revision           |
    `> | taxonomy_term_revision__parent   |
    `> | user__roles                      |
    `> | user__user_picture               |
    `> | users                            |
    `> | users_data                       |
    `> | users_field_data                 |
    `> | watchdog                         |
    `> +----------------------------------+
```

### [Dump Drupal DataBases](https://mariadb.com/kb/en/mariadb-dump/)

```bash
[INPUT]
mysqldump -u bn_drupal -p bitnami_drupal > dump_file.sql

[OUTPUT]
> ls
```

### Create the new Data base on RDS

```sql
[INPUT]
mysql -h dbi-devopsteam07.cshki92s4w5p.eu-west-3.rds.amazonaws.com -u admin -p
CREATE DATABASE bitnami_drupal;
```

### [Import dump in RDS db-instance](https://mariadb.com/kb/en/restoring-data-from-dump-files/)

Note : you can do this from the Drupal Instance. Do not forget to set the "-h" parameter.

```sql
[INPUT]
mysql -h dbi-devopsteam07.cshki92s4w5p.eu-west-3.rds.amazonaws.com -u admin -p

[OUTPUT]
mysql: Deprecated program name. It will be removed in a future release, use '/opt/bitnami/mariadb/bin/mariadb' instead
Enter password: 
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 39
Server version: 10.11.7-MariaDB managed by https://aws.amazon.com/rds/

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| bitnami_drupal     |
| information_schema |
| innodb             |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
6 rows in set (0.001 sec)

MariaDB [(none)]> use bitnami_drupal;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
MariaDB [bitnami_drupal]> show tables;
+----------------------------------+
| Tables_in_bitnami_drupal         |
+----------------------------------+
| block_content                    |
| block_content__body              |
| block_content_field_data         |
| block_content_field_revision     |
| block_content_revision           |
| block_content_revision__body     |
| cache_bootstrap                  |
| cache_config                     |
| cache_container                  |
| cache_data                       |
| cache_default                    |
| cache_discovery                  |
| cache_dynamic_page_cache         |
| cache_entity                     |
| cache_menu                       |
| cache_page                       |
| cache_render                     |
| cache_toolbar                    |
| cachetags                        |
| comment                          |
| comment__comment_body            |
| comment_entity_statistics        |
| comment_field_data               |
| config                           |
| file_managed                     |
| file_usage                       |
| flood                            |
| help_search_items                |
| history                          |
| key_value                        |
| key_value_expire                 |
| menu_link_content                |
| menu_link_content_data           |
| menu_link_content_field_revision |
| menu_link_content_revision       |
| menu_tree                        |
| node                             |
| node__body                       |
| node__comment                    |
| node__field_image                |
| node__field_tags                 |
| node_access                      |
| node_field_data                  |
| node_field_revision              |
| node_revision                    |
| node_revision__body              |
| node_revision__comment           |
| node_revision__field_image       |
| node_revision__field_tags        |
| path_alias                       |
| path_alias_revision              |
| queue                            |
| router                           |
| search_dataset                   |
| search_index                     |
| search_total                     |
| semaphore                        |
| sequences                        |
| sessions                         |
| shortcut                         |
| shortcut_field_data              |
| shortcut_set_users               |
| taxonomy_index                   |
| taxonomy_term__parent            |
| taxonomy_term_data               |
| taxonomy_term_field_data         |
| taxonomy_term_field_revision     |
| taxonomy_term_revision           |
| taxonomy_term_revision__parent   |
| user__roles                      |
| user__user_picture               |
| users                            |
| users_data                       |
| users_field_data                 |
| watchdog                         |
+----------------------------------+
75 rows in set (0.001 sec)
```

### [Get the current Drupal connection string parameters](https://www.drupal.org/docs/8/api/database-api/database-configuration)

```bash
[INPUT]
//help : same settings.php as before
cat /bitnami/drupal/sites/default/settings.php

[OUTPUT]
//at the end of the file you will find connection string parameters
$databases['default']['default'] = array (
  'database' => 'bitnami_drupal',
  'username' => 'bn_drupal',
  'password' => '2b9defd18a354804a1d4c4742c252fb39d808c12cfc2046ffc8f31432ae8a060',
  'prefix' => '',
  'host' => '127.0.0.1',
  'port' => '3306',
  'isolation_level' => 'READ COMMITTED',
  'driver' => 'mysql',
  'namespace' => 'Drupal\\mysql\\Driver\\Database\\mysql',
  'autoload' => 'core/modules/mysql/src/Driver/Database/mysql/',
);
```

### Replace the current host with the RDS FQDN

```
//settings.php

$databases['default']['default'] = array (
   [...] 
   'host' => 'dbi-devopsteam07.cshki92s4w5p.eu-west-3.rds.amazonaws.com',
   [...] 
);
```

### [Create the Drupal Users on RDS Data base](https://mariadb.com/kb/en/create-user/)

Note : only calls from both private subnets must be approved.
* [By Password](https://mariadb.com/kb/en/create-user/#identified-by-password)
* [Account Name](https://mariadb.com/kb/en/create-user/#account-names)
* [Network Mask](https://cric.grenoble.cnrs.fr/Administrateurs/Outils/CalculMasque/)

```sql
[INPUT]
mysql -h dbi-devopsteam07.cshki92s4w5p.eu-west-3.rds.amazonaws.com -u admin -p
CREATE USER bn_drupal@'10.0.7.0/28' IDENTIFIED BY 'devopsteam07';

MariaDB [(none)]> CREATE USER bn_drupal@'10.0.7.0/28' IDENTIFIED BY 'devopsteam07';
Query OK, 0 rows affected (0.005 sec)

MariaDB [(none)]> GRANT ALL PRIVILEGES ON bitnami_drupal.* TO 'bn_drupal'@'10.0.7.0/28';
Query OK, 0 rows affected (0.003 sec)

MariaDB [(none)]> FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.001 sec)

MariaDB [(none)]> SELECT user, host FROM mysql.user WHERE user = 'bn_drupal';
+-----------+-------------+
| User      | Host        |
+-----------+-------------+
| bn_drupal | 10.0.7.0/28 |
+-----------+-------------+
1 row in set (0.004 sec)

MariaDB [(none)]> SHOW GRANTS FOR 'bn_drupal'@'10.0.7.0/28';
+--------------------------------------------------------------------------------------------------------------------+
| Grants for bn_drupal@10.0.7.0/28                                                                                   |
+--------------------------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO `bn_drupal`@`10.0.7.0/28` IDENTIFIED BY PASSWORD '*9F13F90673AB88081D439822B7A6454EA6DD45A6' |
| GRANT ALL PRIVILEGES ON `bitnami_drupal`.* TO `bn_drupal`@`10.0.7.0/28`                                            |
+--------------------------------------------------------------------------------------------------------------------+
2 rows in set (0.000 sec)
```

```sql
//validation
[INPUT]
SHOW GRANTS for 'bn_drupal'@'10.0.[XX].0/[yourMask]]';

[OUTPUT]
+----------------------------------------------------------------------------------------------------------------------------------+
| Grants for <yourNewUser>                                                                                                         |
+----------------------------------------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO <yourNewUser> IDENTIFIED BY PASSWORD 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'                           |
| GRANT ALL PRIVILEGES ON `bitnami_drupal`.* TO <yourNewUser>                                                                      |
+----------------------------------------------------------------------------------------------------------------------------------+
```

### Validate access (on the drupal instance)

```sql
[INPUT]
mariadb -h dbi-devopsteam07.cshki92s4w5p.eu-west-3.rds.amazonaws.com -u bn_drupal -p 
Password > devopsteam07 
MariaDB [(none)]> SHOW DATABASES;

[OUTPUT]
+--------------------+
| Database           |
+--------------------+
| bitnami_drupal     |
| information_schema |
+--------------------+
2 rows in set (0.001 sec)
```

* Repeat the procedure to enable the instance on subnet 2 to also talk to your RDS instance.
