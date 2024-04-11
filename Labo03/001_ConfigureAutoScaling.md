# Task 001 - Configure Auto Scaling

![Schema](./img/CLD_AWS_INFA.PNG)

* Follow the instructions in the tutorial [Getting started with Amazon EC2 Auto Scaling](https://docs.aws.amazon.com/autoscaling/ec2/userguide/GettingStartedTutorial.html) to create a launch template.

* [CLI Documentation](https://docs.aws.amazon.com/cli/latest/reference/autoscaling/)

## Pre-requisites

* Networks (RTE-TABLE/SECURITY GROUP) set as at the end of the Labo2.
* 1 AMI of your Drupal instance
* 0 existing ec2 (even is in a stopped state)
* 1 RDS Database instance - started
* 1 Elastic Load Balancer - started

## Create a new launch configuration. 

|Key|Value|
|:--|:--|
|Name|LT-DEVOPSTEAM[XX]|
|Version|v1.0.0|
|Tag|Name->same as template's name|
|AMI|Your Drupal AMI|
|Instance type|t3.micro (as usual)|
|Subnet|Your subnet A|
|Security groups|Your Drupal Security Group|
|IP Address assignation|Do not assign|
|Storage|Only 10 Go Storage (based on your AMI)|
|Advanced Details/EC2 Detailed Cloud Watch|enable|
|Purchase option/Request Spot instance|disable|

```
[INPUT]
aws ec2 create-launch-template \
--launch-template-name LT-DEVOPSTEAM07 \
--version-description v1.0.0 \
--launch-template-data '{
  "ImageId": "[Your Drupal AMI]",
  "InstanceType": "t3.micro",
  "KeyName": "[Your key pair name]",
  "SecurityGroupIds": ["[Your Drupal Security Group]"],
  "SubnetId": "[Your Subnet A]",
  "TagSpecifications": [
    {
      "ResourceType": "instance",
      "Tags": [
        {
          "Key": "Name",
          "Value": "LT-DEVOPSTEAM07"
        }
      ]
    }
  ],
  "BlockDeviceMappings": [
    {
      "DeviceName": "/dev/sda1",
      "Ebs": {
        "VolumeSize": 10
      }
    }
  ],
  "Monitoring": {
    "Enabled": true
  }
}'

[OUTPUT]
```

## Create an autoscaling group

* Choose launch template or configuration

|Specifications|Key|Value|
|:--|:--|:--|
|Launch Configuration|Name|ASGRP_DEVOPSTEAM[XX]|
||Launch configuration|Your launch configuration|
|Instance launch option|VPC|Refer to infra schema|
||AZ and subnet|AZs and subnets a + b|
|Advanced options|Attach to an existing LB|Your ELB|
||Target group|Your target group|
|Health check|Load balancing health check|Turn on|
||health check grace period|10 seconds|
|Additional settings|Group metrics collection within Cloud Watch|Enable|
||Health check grace period|10 seconds|
|Group size and scaling option|Desired capacity|1|
||Min desired capacity|1|
||Max desired capacity|4|
||Policies|Target tracking scaling policy|
||Target tracking scaling policy Name|TTP_DEVOPSTEAM[XX]|
||Metric type|Average CPU utilization|
||Target value|50|
||Instance warmup|30 seconds|
||Instance maintenance policy|None|
||Instance scale-in protection|None|
||Notification|None|
|Add tag to instance|Name|AUTO_EC2_PRIVATE_DRUPAL_DEVOPSTEAM[XX]|

```
[INPUT]
aws autoscaling create-auto-scaling-group \
--auto-scaling-group-name ASGRP_DEVOPSTEAM07 \
--launch-configuration-name LT-DEVOPSTEAM07 \
--min-size 1 \
--max-size 4 \
--desired-capacity 1 \
--vpc-zone-identifier "subnet-0ce0d90738ee34415, subnet-013e922d28dc16192" \
--load-balancer-names "ELB-DEVOPSTEAM07" \
--health-check-type ELB \
--health-check-grace-period 10 \
--tags "Key=Name,Value=AUTO_EC2_PRIVATE_DRUPAL_DEVOPSTEAM07"

# Créer la politique de suivi de la cible
aws autoscaling put-scaling-policy \
--auto-scaling-group-name ASGRP_DEVOPSTEAM07 \
--policy-name TTP_DEVOPSTEAM07 \
--policy-type TargetTrackingScaling \
--target-tracking-configuration '{
  "PredefinedMetricSpecification": {
    "PredefinedMetricType": "ASGAverageCPUUtilization"
  },
  "TargetValue": 50,
  "InstanceWarmup": 30
}'

[OUTPUT]
```

* Result expected

The first instance is launched automatically.

Test ssh and web access.

```
[INPUT]
ssh devopsteam07@15.188.43.46 -i CLD_KEY_DMZ_DEVOPSTEAM07.pem -L 2223:10.0.7.4:22
ssh bitnami@localhost -p 2223 -i CLD_KEY_DRUPAL_DEVOPSTEAM07.pem

[OUTPUT]
[4:58 PM] Slimani Walid
The authenticity of host '[localhost]:2223 ([::1]:2223)' can't be established.

ECDSA key fingerprint is SHA256:5kqpXy/fhscyIpMAvdflWSBmsarabVSe4ZshRTHLBEE.

Are you sure you want to continue connecting (yes/no/[fingerprint])? yes

Warning: Permanently added '[localhost]:2223' (ECDSA) to the list of known hosts.

Linux ip-10-0-7-4 5.10.0-28-cloud-amd64 #1 SMP Debian 5.10.209-2 (2024-01-31) x86_64
 
The programs included with the Debian GNU/Linux system are free software;

the exact distribution terms for each program are described in the

individual files in /usr/share/doc/*/copyright.
 
Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent

permitted by applicable law.

       ___ _ _                   _

      | _ |_) |_ _ _  __ _ _ __ (_)

      | _ \ |  _| ' \/ _` | '  \| |

      |___/_|\__|_|_|\__,_|_|_|_|_|
 
  *** Welcome to the Bitnami package for Drupal 10.2.3-1        ***

  *** Documentation:  https://docs.bitnami.com/aws/apps/drupal/ ***

  ***                 https://docs.bitnami.com/aws/             ***

  *** Bitnami Forums: https://github.com/bitnami/vms/           ***

Last login: Thu Mar 28 13:11:03 2024 from 10.0.0.5
Bitnami package for Drupal for AWS Cloud
```
 [Capture d'écran du test ssh](./img/test_ssh.jpg)
