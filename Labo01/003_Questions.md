* What is the smallest and the biggest instance type (in terms of
  virtual CPUs and memory) that you can choose from when creating an
  instance?
 
```
Smallest and Biggest Instance Types: The smallest and largest EC2 instance types can vary based on the region and availability. The smallest instance type is typically something like a t2.nano, which offers 1 vCPU and 0.5 GB of memory.
The largest instance types can be part of the u- series, like u-24tb1.metal, offering hundreds of vCPUs and several terabytes of RAM.
 
```
[AWS EC2 Instance Types](https://aws.amazon.com/ec2/instance-types/)  
[AWS EC2 High Memory Instances](https://aws.amazon.com/ec2/instance-types/high-memory/)
 
* How long did it take for the new instance to get into the _running_ state?
 
```
Time for Instance to Reach 'Running' State: The time it takes for an EC2 instance to transition to the 'running' state varies. Generally, it takes a few minutes, but this can be influenced by the instance type, the AMI used, the configuration, and the current load on AWS. When you launch an instance, it enters the pending state. The instance type that you specified at launch determines the hardware of the host computer for your instance. We use the Amazon Machine Image (AMI) you specified at launch to boot the instance. After the instance is ready, it enters the running state. As soon as your instance transitions to the running state, you're billed for each second, with a one-minute minimum, that you keep the instance running, even if the instance remains idle and you don't connect to it.
```
[AWS EC2 Instance Life](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-lifecycle.html)
 
 
* Using the commands to explore the machine listed earlier, respond to
  the following questions and explain how you came to the answer:
 
    * What's the difference between time here in Switzerland and the time set on
      the machine?
```
Time Difference: You can find the time difference by comparing the local time in Switzerland (CEST or CET depending on the time of the year) with the time reported by the instance. Use the date command in the instance to check its time.
Once connected, run the date command to display the current date and time set on the instance: date
devopsteam07@ip-10-0-0-5:/sys/hypervisor$ date
Thu Mar 14 10:36:06 UTC 2024
```
 
    * What's the name of the hypervisor?
```
Hypervisor Name: Run cat /sys/hypervisor/type on the EC2 instance to find out the name of the hypervisor.
devopsteam07@ip-10-0-0-5:~$ cat /sys/hypervisor/type
xen
```
 
    * How much free space does the disk have?
```
Disk Space: Use the df -h command to check the disk space. It will list the available, used, and total space on all mounted filesystems.
devopsteam07@ip-10-0-0-5:/sys/hypervisor$ df -h
Filesystem      Size  Used Avail Use% Mounted on
udev            476M     0  476M   0% /dev
tmpfs            98M  500K   97M   1% /run
/dev/xvda1      7.7G  1.4G  5.9G  19% /
tmpfs           488M     0  488M   0% /dev/shm
tmpfs           5.0M     0  5.0M   0% /run/lock
/dev/xvda15     124M   12M  113M  10% /boot/efi
tmpfs            98M     0   98M   0% /run/user/1019
tmpfs            98M     0   98M   0% /run/user/1009
tmpfs            98M     0   98M   0% /run/user/1007
```
 
 
* Try to ping the instance ssh srv from your local machine. What do you see?
  Explain. Change the configuration to make it work. Ping the
  instance, record 5 round-trip times.
 
```
If you cannot ping the instance, it might be due to security group settings that block ICMP traffic (used for ping). Modify the instance's security group to allow ICMP traffic.
After changing the settings, you can ping the instance and record the round-trip times using the ping command.
bitnami@ip-10-0-7-10:~$ ping 10.0.0.5 -c 5
PING 10.0.0.5 (10.0.0.5) 56(84) bytes of data.

--- 10.0.0.5 ping statistics ---
5 packets transmitted, 0 received, 100% packet loss, time 4092ms
```
 
* Determine the IP address seen by the operating system in the EC2
  instance by running the `ifconfig` command. What type of address
  is it? Compare it to the address displayed by the ping command
  earlier. How do you explain that you can successfully communicate
  with the machine?
 
```
Run ifconfig (or ip addr show) to find the internal IP address of the EC2 instance. This is likely a private IP address.
Compare this to the IP address you used to ping the instance, which is likely a public IP address.
The difference is because AWS uses Network Address Translation (NAT) to map public IP addresses to private ones within its network, enabling instances to communicate with the internet.
```
