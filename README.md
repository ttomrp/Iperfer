## Testing client/server connection locally
You can establish a TCP connection between a client and server running out of two different shells on your local computer, which is easier than testing on the mininet VM. 
### Get private IP address
* Open Command Prompt and type "ipconfig"
* Your private IP address should appear in an "IPv4 Address" field and will start with a "10"
### Run Iperfer using your private IP address
* Open two shells (two Command Prompt windows); one will be your server and the other will be your client
* Change directories (cd) into your project folder
* At the command line of your server, start Iperfer using any port number in the expected range (1024 - 65535)
* At the command line of your client, start Iperfer using your private IP address for the hostname and the port you specified when you started Iperfer on your server
