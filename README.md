# Discord Shopping List Bot
### Authored by Henry Li

## Installation
#### Step 1: Creating the Discord Bot

1. Next, you need to create a new bot on the Discord developer portal and get your bot token:
2. Go to the Discord Developer Portal
3. Click on "New Application" and give it a name.
4. Go to the "Bot" tab and click "Add Bot".
5. Under the "Token" section, click "Copy". This is your bot token.

#### Step 2: Connecting the Bot to your Server
1. Go to the "OAuth2" tab in the Discord Developer Portal.
2. Under "Scopes", select "bot".
3. Under "Bot Permissions", select the permissions your bot needs.
   - Send Messages
   - Manage Messages
   - Embed Links
   - Read Message History
   - Use External Emojis
   - Add Reactions
   - Use Slash Commands
4. Copy the generated URL and open it in your web browser to invite the bot to your server.

#### Step 3: Add repo locally
- Clone the github repo 
- Run the application on your IDE

## Usage
- All instances are saved in-memory. Once you quit the application, your instances will disappear

## Visuals
Some examples of using the slash commands
- /create Costco
  - ![/create](/src/main/resources/Images/create-shop.png)
  
- /add Cheese, /add Basmati Rice, /add Chicken Breast, /add Eggs
  - ![/add](/src/main/resources/Images/add-items.png)

- /update Costco Cheese PepperJ Jack Cheese
  - ![/update](/src/main/resources/Images/update-cheese.png)
  
- /remove Costco Eggs
  - ![/remove](/src/main/resources/Images/remove-eggs.png)

## Roadmap
- Host the application on some server to ensure the bot is running during specific times of day
- Being able to delete a Shopping List by reacting to the embedded link's "X" unicode emoji
- Make the arguments for the slash commands not case-sensitive

## Resources
- Development using Javacord's API Version 3.8.0 
  - https://javacord.org/wiki/