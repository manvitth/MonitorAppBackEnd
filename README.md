# MonitorApplication
2 TABLES
ipPort entities ip_address , portno
userDetails entities email , tokenid, ip_port_id(one to one) mapped with ipPort TABLE ,
HENCE SAME IPADDRESS AND PORT NO IS ADDED IN BOTH TABLES WITH SAME ID

Scheduler = 1 sec

JSON BODY 
Eg: /adddetails

{
  "token_id": "abcdefghijk1234567",
  "email": "meganitish@gmail.com",
  "ipPort": {
  "ipAddress": "172.16.3.250" ,
    "port": 6969
  }
}
