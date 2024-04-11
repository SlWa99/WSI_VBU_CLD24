# Task 003 - Test and validate the elasticity

![Schema](./img/CLD_AWS_INFA.PNG)


## Simulate heavy load to trigger a scaling action

* [Install the package "stress" on your Drupal instance](https://www.geeksforgeeks.org/linux-stress-command-with-examples/)

* [Install the package htop on your Drupal instance](https://www.geeksforgeeks.org/htop-command-in-linux-with-examples/)

* Check how many vCPU are available (with htop command)

```
[INPUT]
htop

[OUTPUT]
```
[htop](./img/htop.jpg)

### Stress your instance

```
[INPUT]
stress --cpu 2 -v --timeout 30s --vm 4 --io 1

[OUTPUT]
```
[stress](./img/stress.jpg)

* (Scale-IN) Observe the autoscaling effect on your infa


```
[INPUT]
```
[cpu](./img/cpu.png)

[Sample](./img/CLD_AWS_CLOUDWATCH_CPU_METRICS.PNG)

[liste](./img/liste_instances.png)  
[Sample](./img/CLD_AWS_EC2_LIST.PNG)

```
//TODO Validate that the various instances have been distributed between the two available az.
[INPUT]
aws ec2 describe-instances \
    --query "Reservations[*].Instances[*].[InstanceId,Placement.AvailabilityZone]" \
    --filters "Name=tag:aws:autoscaling:groupName,Values=ASGRP_DEVOPSTEAM07"

[OUTPUT]
[
    [
        [
            "i-0c5437dc716fe2336",
            "eu-west-3a"
        ]
    ],
    [
        [
            "i-04c584ac766f70234",
            "eu-west-3a"
        ]
    ],
    [
        [
            "i-06cea7e6570c47e5a",
            "eu-west-3a"
        ]
    ],
    [
        [
            "i-0a39ef2c793930599",
            "eu-west-3b"
        ]
    ],
    [
        [
            "i-068becc36fd0b2956",
            "eu-west-3b"
        ]
    ]
]
```

```

```
[history](./img/history.png)

[Sample](./img/CLD_AWS_ASG_ACTIVITY_HISTORY.PNG)

```
```
[cpu](./img/cpu.png)

[Sample](./img/CLD_AWS_CLOUDWATCH_ALARMHIGH_STATS.PNG)


* (Scale-OUT) As soon as all 4 instances have started, end stress on the main machine.

[Change the default cooldown period](https://docs.aws.amazon.com/autoscaling/ec2/userguide/ec2-auto-scaling-scaling-cooldowns.html)

```

```
[cpu](./img/cpu.png)

```
```
[liste](./img/liste_instances.png)

```
```
[history](./img/history.png)

## Release Cloud resources

Once you have completed this lab release the cloud resources to avoid
unnecessary charges:

* Terminate the EC2 instances.
    * Make sure the attached EBS volumes are deleted as well.
* Delete the Auto Scaling group.
* Delete the Elastic Load Balancer.
* Delete the RDS instance.

(this last part does not need to be documented in your report.)

## Delivery

Inform your teacher of the deliverable on the repository (link to the commit to retrieve)
