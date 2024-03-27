### Deploy the elastic load balancer

In this task you will create a load balancer in AWS that will receive
the HTTP requests from clients and forward them to the Drupal
instances.

![Schema](./img/CLD_AWS_INFA.PNG)

## Task 01 Prerequisites for the ELB

* Create a dedicated security group

|Key|Value|
|:--|:--|
|Name|SG-DEVOPSTEAM[XX]-LB|
|Inbound Rules|Application Load Balancer|
|Outbound Rules|Refer to the infra schema|

```bash
[INPUT]
aws ec2 create-security-group --group-name "SG-DEVOPSTEAM07-LB" --description "SG-DEVOPSTEAM07-LB" --vpc-id vpc-03d46c285a2af77ba

[OUTPUT]
{
    "GroupId": "sg-0e21977ead9a6a399"
}
```

* Create the Target Group

|Key|Value|
|:--|:--|
|Target type|Instances|
|Name|TG-DEVOPSTEAM[XX]|
|Protocol and port|Refer to the infra schema|
|Ip Address type|IPv4|
|VPC|Refer to the infra schema|
|Protocol version|HTTP1|
|Health check protocol|HTTP|
|Health check path|/|
|Port|Traffic port|
|Healthy threshold|2 consecutive health check successes|
|Unhealthy threshold|2 consecutive health check failures|
|Timeout|5 seconds|
|Interval|10 seconds|
|Success codes|200|

```bash
[INPUT]
aws elbv2 create-target-group \
--name TG-DEVOPSTEAM07 \
--protocol HTTP \
--protocol-version HTTP1 \
--port 8080 \
--vpc-id vpc-03d46c285a2af77ba \
--health-check-protocol HTTP \
--health-check-port "traffic-port" \
--health-check-path "/" \
--healthy-threshold-count 2 \
--unhealthy-threshold-count 2 \
--health-check-timeout-seconds 5 \
--health-check-interval-seconds 10 \
--matcher HttpCode=200 \
--target-type instance

[OUTPUT]
{
    "TargetGroups": [
        {
            "TargetGroupArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:targetgroup/TG-DEVOPSTEAM07/fb693fdf39d55075",
            "TargetGroupName": "TG-DEVOPSTEAM07",
            "Protocol": "HTTP",
            "Port": 8080,
            "VpcId": "vpc-03d46c285a2af77ba",
            "HealthCheckProtocol": "HTTP",
            "HealthCheckPort": "traffic-port",
            "HealthCheckEnabled": true,
            "HealthCheckIntervalSeconds": 10,
            "HealthCheckTimeoutSeconds": 5,
            "HealthyThresholdCount": 2,
            "UnhealthyThresholdCount": 2,
            "HealthCheckPath": "/",
            "Matcher": {
                "HttpCode": "200"
            },
            "TargetType": "instance",
            "ProtocolVersion": "HTTP1",
            "IpAddressType": "ipv4"
        }
    ]
}
```


## Task 02 Deploy the Load Balancer

[Source](https://aws.amazon.com/elasticloadbalancing/)

* Create the Load Balancer

|Key|Value|
|:--|:--|
|Type|Application Load Balancer|
|Name|ELB-DEVOPSTEAM99|
|Scheme|Internal|
|Ip Address type|IPv4|
|VPC|Refer to the infra schema|
|Security group|Refer to the infra schema|
|Listeners Protocol and port|Refer to the infra schema|
|Target group|Your own target group created in task 01|

Provide the following answers (leave any
field not mentioned at its default value):

```bash
// Register drupal instances in ELB
[INPUT]
aws elbv2 register-targets --target-group-arn arn:aws:elasticloadbalancing:eu-west-3:709024702237:targetgroup/TG-DEVOPSTEAM07/fb693fdf39d55075 --targets Id=i-0129fd22ea338471a Id=i-0af9a51f45e4dfed2

// Create de Load balancer
[INPUT]
aws elbv2 create-load-balancer --name ELB-DEVOPSTEAM07 --subnets subnet-013e922d28dc16192 subnet-0ce0d90738ee34415 --scheme internal --type application --security-groups sg-0e21977ead9a6a399 --ip-address-type ipv4

[OUTPUT]
{
    "LoadBalancers": [
        {
            "LoadBalancerArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:loadbalancer/app/ELB-DEVOPSTEAM07/22900b4386820b34",
            "DNSName": "internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com",
            "CanonicalHostedZoneId": "Z3Q77PNBQS71R4",
            "CreatedTime": "2024-03-21T16:52:34.580000+00:00",
            "LoadBalancerName": "ELB-DEVOPSTEAM07",
            "Scheme": "internal",
            "VpcId": "vpc-03d46c285a2af77ba",
            "State": {
                "Code": "provisioning"
            },
            "Type": "application",
            "AvailabilityZones": [
                {
                    "ZoneName": "eu-west-3a",
                    "SubnetId": "subnet-013e922d28dc16192",
                    "LoadBalancerAddresses": []
                },
                {
                    "ZoneName": "eu-west-3b",
                    "SubnetId": "subnet-0ce0d90738ee34415",
                    "LoadBalancerAddresses": []
                }
            ],
            "SecurityGroups": [
                "sg-0e21977ead9a6a399"
            ],
            "IpAddressType": "ipv4"
        }
    ]
}

// Create the listener
[INPUT]
aws elbv2 create-listener \
--load-balancer-arn arn:aws:elasticloadbalancing:eu-west-3:709024702237:loadbalancer/app/ELB-DEVOPSTEAM07/22900b4386820b34 \
--protocol HTTP \
--port 8080 \
--default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:eu-west-3:709024702237:targetgroup/TG-DEVOPSTEAM07/fb693fdf39d55075

[OUTPUT]
{
    "Listeners": [
        {
            "ListenerArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:listener/app/ELB-DEVOPSTEAM07/22900b4386820b34/55f946f6aad03532",
            "LoadBalancerArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:loadbalancer/app/ELB-DEVOPSTEAM07/22900b4386820b34",
            "Port": 8080,
            "Protocol": "HTTP",
            "DefaultActions": [
                {
                    "Type": "forward",
                    "TargetGroupArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:targetgroup/TG-DEVOPSTEAM07/fb693fdf39d55075",
                    "ForwardConfig": {
                        "TargetGroups": [
                            {
                                "TargetGroupArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:targetgroup/TG-DEVOPSTEAM07/fb693fdf39d55075",
                                "Weight": 1
                            }
                        ],
                        "TargetGroupStickinessConfig": {
                            "Enabled": false
                        }
                    }
                }
            ]
        }
    ]
}
```

* Get the ELB FQDN (DNS NAME - A Record)

```bash
[INPUT]
aws elbv2 describe-load-balancers --names ELB-DEVOPSTEAM07

[OUTPUT]
{
    "LoadBalancers": [
        {
            "LoadBalancerArn": "arn:aws:elasticloadbalancing:eu-west-3:709024702237:loadbalancer/app/ELB-DEVOPSTEAM07/22900b4386820b34",
            "DNSName": "internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com",
            "CanonicalHostedZoneId": "Z3Q77PNBQS71R4",
            "CreatedTime": "2024-03-21T16:52:34.580000+00:00",
            "LoadBalancerName": "ELB-DEVOPSTEAM07",
            "Scheme": "internal",
            "VpcId": "vpc-03d46c285a2af77ba",
            "State": {
                "Code": "active"
            },
            "Type": "application",
            "AvailabilityZones": [
                {
                    "ZoneName": "eu-west-3a",
                    "SubnetId": "subnet-013e922d28dc16192",
                    "LoadBalancerAddresses": []
                },
                {
                    "ZoneName": "eu-west-3b",
                    "SubnetId": "subnet-0ce0d90738ee34415",
                    "LoadBalancerAddresses": []
                }
            ],
            "SecurityGroups": [
                "sg-0e21977ead9a6a399"
            ],
            "IpAddressType": "ipv4"
        }
    ]
}

// Inbound rule added to SG-DEVOPSTEAM07-LB
[INPUT]
aws ec2 authorize-security-group-ingress --group-id sg-0e21977ead9a6a399 --ip-permissions IpProtocol=tcp,FromPort=8080,ToPort=8080,IpRanges='[{CidrIp=10.0.0.0/28, Description="Allow HTTP from DMZ"}]'

[OUTPUT]
{
    "Return": true,
    "SecurityGroupRules": [
        {
            "SecurityGroupRuleId": "sgr-0727dbdf6beb45e2b",
            "GroupId": "sg-0e21977ead9a6a399",
            "GroupOwnerId": "709024702237",
            "IsEgress": false,
            "IpProtocol": "tcp",
            "FromPort": 8080,
            "ToPort": 8080,
            "CidrIpv4": "10.0.0.0/28",
            "Description": "Allow HTTP from DMZ"
        }
    ]
}
```

* Get the ELB deployment status

Note : In the EC2 console select the Target Group. In the
       lower half of the panel, click on the **Targets** tab. Watch the
       status of the instance go from **unused** to **initial**.

* Ask the DMZ administrator to register your ELB with the reverse proxy via the private teams channel

* Update your string connection to test your ELB and test it

```bash
//connection string updated
ssh devopsteam07@15.188.43.46 -i CLD_KEY_DMZ_DEVOPSTEAM07.pem -Nv -L 8080:internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com:8080
```

* Test your application through your ssh tunneling

```bash
[INPUT]
curl localhost:8080

[OUTPUT]
<!DOCTYPE html>
<html lang="en" dir="ltr" style="--color--primary-hue:202;--color--primary-saturation:79%;--color--primary-lightness:50">
  <head>
    <meta charset="utf-8" />
<meta name="Generator" content="Drupal 10 (https://www.drupal.org)" />
<meta name="MobileOptimized" content="width" />
<meta name="HandheldFriendly" content="true" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="icon" href="/core/themes/olivero/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="alternate" type="application/rss+xml" title="" href="http://localhost:8080/rss.xml" />
<link rel="alternate" type="application/rss+xml" title="" href="http://localhost/rss.xml" />

    <title>Welcome! | My blog</title>
    
    
...
```

#### Questions - Analysis

* On your local machine resolve the DNS name of the load balancer into
  an IP address using the `nslookup` command (works on Linux, macOS and Windows). Write
  the DNS name and the resolved IP Address(es) into the report.

```
[INPUT]
nslookup internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com

[OUTPUT]
Server:		172.20.10.1
Address:	172.20.10.1#53

Non-authoritative answer:
Name:	internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com
Address: 10.0.7.5
Name:	internal-ELB-DEVOPSTEAM07-1968309273.eu-west-3.elb.amazonaws.com
Address: 10.0.7.135
```

* From your Drupal instance, identify the ip from which requests are sent by the Load Balancer.

Help : execute `tcpdump port 8080`

```
[INPUT]
sudo tcpdump port 8080

[OUTPUT]
10.0.7.5.20266 > 10.0.7.140.http-alt: Flags [P.], cksum 0xd0cf (correct), seq 1:131, ack 1, win 106, options [nop,nop,TS val 415271772 ecr 1532089568], length 130: HTTP, length: 130
	GET / HTTP/1.1
	Host: 10.0.7.140:8080
	Connection: close
	User-Agent: ELB-HealthChecker/2.0
	Accept-Encoding: gzip, compressed
	
21:25:18.477710 IP (tos 0x0, ttl 64, id 3020, offset 0, flags [DF], proto TCP (6), length 52)
    10.0.7.140.http-alt > 10.0.7.5.20266: Flags [.], cksum 0x22b7 (incorrect -> 0x089e), ack 131, win 489, options [nop,nop,TS val 1532089569 ecr 415271772], length 0
21:25:18.506675 IP (tos 0x0, ttl 64, id 3021, offset 0, flags [DF], proto TCP (6), length 5674)
    10.0.7.140.http-alt > 10.0.7.5.20266: Flags [P.], cksum 0x38ad (incorrect -> 0xf00e), seq 1:5623, ack 131, win 489, options [nop,nop,TS val 1532089598 ecr 415271772], length 5622: HTTP, length: 5622
	HTTP/1.1 200 OK
	Date: Thu, 21 Mar 2024 21:16:04 GMT
	Server: Apache
	Cache-Control: must-revalidate, no-cache, private
	X-Drupal-Dynamic-Cache: MISS
	Content-language: en
	X-Content-Type-Options: nosniff
	X-Frame-Options: SAMEORIGIN
	Expires: Sun, 19 Nov 1978 05:00:00 GMT
	X-Generator: Drupal 10 (https://www.drupal.org)
	X-Drupal-Cache: HIT
	Vary: Accept-Encoding
	Content-Encoding: gzip
	Content-Length: 5147
	Connection: close
	Content-Type: text/html; charset=UTF-8


**10.0.7.5** is the source IP address that sends requests to the instance. In this context, it is the IP address of the ELB that performs health checks on the Drupal instance. 

The following line specifically indicates a health check request from the ELB `**User-Agent:** ELB-HealthChecker/2.0`

**10.0.7.140 is the destination IP address (the Drupal instance) which receives requests on port **http-alt** (port 8080).
```

* In the Apache access log identify the health check accesses from the
  load balancer and copy some samples into the report.

```
[INPUT]
sudo find / -type d -name 'apache'
cat /opt/bitnami/apache/logs/access_log

[OUTPUT]
10.0.7.10 - - [19/Mar/2024:19:36:44 +0000] "GET / HTTP/1.1" 200 16554
10.0.0.5 - - [19/Mar/2024:19:37:22 +0000] "GET / HTTP/1.1" 200 16554
10.0.7.140 - - [20/Mar/2024:21:08:51 +0000] "GET / HTTP/1.1" 500 67
127.0.0.1 - - [20/Mar/2024:20:57:56 +0000] "GET / HTTP/1.1" 500 67
10.0.7.135 - - [21/Mar/2024:21:25:21 +0000] "GET / HTTP/1.1" 200 5147
10.0.7.5 - - [21/Mar/2024:21:31:18 +0000] "GET / HTTP/1.1" 200 5147
```
