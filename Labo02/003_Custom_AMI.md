# Custom AMI and Deploy the second Drupal instance

In this task you will update your AMI with the Drupal settings and deploy it in the second availability zone.

## Task 01 - Create AMI

### [Create AMI](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/ec2/create-image.html)

Note : stop the instance before

|Key|Value for GUI Only|
|:--|:--|
|Name|AMI_DRUPAL_DEVOPSTEAM[XX]_LABO02_RDS|
|Description|Same as name value|

```bash
// Stop the instance
[INPUT]
aws ec2 describe-instances --query 'Reservations[*].Instances[*].[InstanceId, State.Name]'

[OUTPUT]
{
    "StoppingInstances": [
        {
            "CurrentState": {
                "Code": 64,
                "Name": "stopping"
            },
            "InstanceId": "i-0129fd22ea338471a",
            "PreviousState": {
                "Code": 16,
                "Name": "running"
            }
        }
    ]
}

[INPUT]
aws ec2 stop-instances --instance-ids i-0129fd22ea338471a

// Create AMI
[INPUT]
aws ec2 create-image \
    --instance-id i-0129fd22ea338471a \
    --name "AMI_DRUPAL_DEVOPSTEAM07_LABO02_RDS" \
    --description "AMI_DRUPAL_DEVOPSTEAM07_LABO02_RDS"

[OUTPUT]
{
    "ImageId": "ami-067a56f4f751aacdc"
}
```

## Task 02 - Deploy Instances

* Restart Drupal Instance in Az1

* Deploy Drupal Instance based on AMI in Az2

|Key|Value for GUI Only|
|:--|:--|
|Name|EC2_PRIVATE_DRUPAL_DEVOPSTEAM[XX]_B|
|Description|Same as name value|

```bash
[INPUT]
aws ec2 start-instances --instance-ids i-0129fd22ea338471a

[OUTPUT]
{
    "StartingInstances": [
        {
            "CurrentState": {
                "Code": 0,
                "Name": "pending"
            },
            "InstanceId": "i-0129fd22ea338471a",
            "PreviousState": {
                "Code": 80,
                "Name": "stopped"
            }
        }
    ]
}

[INPUT]
aws ec2 run-instances --image-id ami-067a56f4f751aacdc --count 1 --instance-type t3.micro --key-name CLD_KEY_DRUPAL_DEVOPSTEAM07 --security-group-ids sg-085ff56fa93e637f3 --subnet-id subnet-0ce0d90738ee34415 --private-ip-address 10.0.7.140  --placement AvailabilityZone=eu-west-3b

[OUTPUT]
{
    "Groups": [],
    "Instances": [
        {
            "AmiLaunchIndex": 0,
            "ImageId": "ami-067a56f4f751aacdc",
            "InstanceId": "i-0af9a51f45e4dfed2",
            "InstanceType": "t3.micro",
            "KeyName": "CLD_KEY_DRUPAL_DEVOPSTEAM07",
            "LaunchTime": "2024-03-20T20:41:01+00:00",
            "Monitoring": {
                "State": "disabled"
            },
            "Placement": {
                "AvailabilityZone": "eu-west-3b",
                "GroupName": "",
                "Tenancy": "default"
            },
            "PrivateDnsName": "ip-10-0-7-140.eu-west-3.compute.internal",
            "PrivateIpAddress": "10.0.7.140",
            "ProductCodes": [],
            "PublicDnsName": "",
            "State": {
                "Code": 0,
                "Name": "pending"
            },
            "StateTransitionReason": "",
            "SubnetId": "subnet-0ce0d90738ee34415",
            "VpcId": "vpc-03d46c285a2af77ba",
            "Architecture": "x86_64",
            "BlockDeviceMappings": [],
            "ClientToken": "2b2c244d-1a3a-4fe8-b355-79baa4e5d872",
            "EbsOptimized": false,
            "EnaSupport": true,
            "Hypervisor": "xen",
            "NetworkInterfaces": [
                {
                    "Attachment": {
                        "AttachTime": "2024-03-20T20:41:01+00:00",
                        "AttachmentId": "eni-attach-0056053c99111e8b0",
                        "DeleteOnTermination": true,
                        "DeviceIndex": 0,
                        "Status": "attaching",
                        "NetworkCardIndex": 0
                    },
                    "Description": "",
                    "Groups": [
                        {
                            "GroupName": "SG-PRIVATE-DRUPAL-DEVOPSTEAM07",
                            "GroupId": "sg-085ff56fa93e637f3"
                        }
                    ],
                    "Ipv6Addresses": [],
                    "MacAddress": "0a:2e:31:a5:4a:dd",
                    "NetworkInterfaceId": "eni-006a8bafd3e1c970f",
                    "OwnerId": "709024702237",
                    "PrivateIpAddress": "10.0.7.140",
                    "PrivateIpAddresses": [
                        {
                            "Primary": true,
                            "PrivateIpAddress": "10.0.7.140"
                        }
                    ],
                    "SourceDestCheck": true,
                    "Status": "in-use",
                    "SubnetId": "subnet-0ce0d90738ee34415",
                    "VpcId": "vpc-03d46c285a2af77ba",
                    "InterfaceType": "interface"
                }
            ],
            "RootDeviceName": "/dev/xvda",
            "RootDeviceType": "ebs",
            "SecurityGroups": [
                {
                    "GroupName": "SG-PRIVATE-DRUPAL-DEVOPSTEAM07",
                    "GroupId": "sg-085ff56fa93e637f3"
                }
            ],
            "SourceDestCheck": true,
            "StateReason": {
                "Code": "pending",
                "Message": "pending"
            },
            "VirtualizationType": "hvm",
            "CpuOptions": {
                "CoreCount": 1,
                "ThreadsPerCore": 2
            },
            "CapacityReservationSpecification": {
                "CapacityReservationPreference": "open"
            },
            "MetadataOptions": {
                "State": "pending",
                "HttpTokens": "optional",
                "HttpPutResponseHopLimit": 1,
                "HttpEndpoint": "enabled",
                "HttpProtocolIpv6": "disabled",
                "InstanceMetadataTags": "disabled"
            },
            "EnclaveOptions": {
                "Enabled": false
            },
            "PrivateDnsNameOptions": {
                "HostnameType": "ip-name",
                "EnableResourceNameDnsARecord": false,
                "EnableResourceNameDnsAAAARecord": false
            },
            "MaintenanceOptions": {
                "AutoRecovery": "default"
            },
            "CurrentInstanceBootMode": "legacy-bios"
        }
    ],
    "OwnerId": "709024702237",
    "ReservationId": "r-0b8c3c235bd76c1cf"
}
```

## Task 03 - Test the connectivity

### Update your ssh connection string to test

* add tunnels for ssh and http pointing on the B Instance

```bash
//updated string connection
ssh devopsteam07@15.188.43.46 -i CLD_KEY_DMZ_DEVOPSTEAM07.pem -L 2223:10.0.7.140:22 -L 8080:10.0.7.140:8080 -L 2224:10.0.7.10:22 -L 8081:10.0.7.10:8080

ssh bitnami@localhost -p 2223 -i CLD_KEY_DRUPAL_DEVOPSTEAM07.pem
ssh bitnami@localhost -p 2224 -i CLD_KEY_DRUPAL_DEVOPSTEAM07.pem
```

## Check SQL Accesses

```sql
[INPUT]
//sql string connection from A

[OUTPUT]
```

```sql
[INPUT]
//sql string connection from B

[OUTPUT]
```

### Check HTTP Accesses

```bash
//connection string updated
[INPUT]
curl http://localhost:8080
curl http://localhost:8081
```

### Read and write test through the web app

* Login in both webapps (same login)

* Change the users' email address on a webapp... refresh the user's profile page on the second and validated that they are communicating with the same db (rds).

* Observations ?

```
Changes are immediately visible on the second web application because both applications use the same database instance.
```

### Change the profil picture

* Observations ?

```
When the profile picture is updated, it is only displayed on the instance where the change was made, leaving an empty thumbnail on the second instance. This is because the uploaded image is stored locally on the file system of the specific instance where the change took place, rather than in the common database. 
```
