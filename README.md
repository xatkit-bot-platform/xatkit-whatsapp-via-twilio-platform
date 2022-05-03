# Xatkit-twilio-platform
Receive and send messages from WhatsApp using the Twilio API
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)



## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |
| Reply | - `message` (**String**): the message to post as a reply <br/> | The posted message | String | Posts the provided `message` as a reply to a received message.|
| ReplyMedia | - `message` (**String**): the message to post as a reply <br/> - `media` (**String**): the URL of the media you want to send  | The posted message | String | Posts the provided `message` as a reply to a received message.|


## Options

The Twilio platform supports the following configuration options

| Key                  | Values | Description                                                  | Constraint    |
| -------------------- | ------ | ------------------------------------------------------------ | ------------- |
| `xatkit.twilio.username` | String | The Account SID of your Twilio Account | Mandatory
| `xatkit.twilio.auth.token` | String | The Twilio token used by Xatkit to interact with the Twilio API.| Mandatory

## Using the Twilio platform

Check the BotTest sample bot included in the repo for an example of a bot using this platform. Make sure also to include this dependency to your pom

```xml
        <dependency>
            <groupId>com.xatkit</groupId>
            <artifactId>twilio-platform</artifactId>
            <version>3.0.1-SNAPSHOT</version>
        </dependency>
```
You also need to configure your Twilio Sandbox for WhatsApp in the Twilio console by adding the enpoint URL (you can use ngrok for this) make sure to add '/twilio' to the end of the URL, example: "http://x-serverurl-x/twilio"

   ![Twilio Sandbox for WhatsApp](https://user-images.githubusercontent.com/48770214/165193307-fdaaf2f9-7c02-4bf2-a0f8-ae4eccb8a9e1.png)

You can get more information about it at https://www.twilio.com/docs/whatsapp/sandbox

