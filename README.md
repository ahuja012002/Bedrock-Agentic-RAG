# Bedrock-Agentic-RAG

Agentic Retrieval Augmented Generation (RAG) applications represent an advanced approach in AI that integrates foundation models (FMs) with external knowledge retrieval and autonomous agent capabilities. These systems dynamically access and process information, break down complex tasks, use external tools, apply reasoning, and adapt to various contexts. They go beyond simple question answering by performing multi-step processes, making decisions, and generating complex outputs.

In this example, we will build an agentic RAG application for a customer support of a manufacturing Company. We have used Bedrock Knowledge Base. Agents in Bedrock backed by Action groups Lambda function . The Agents are coded in Java, Spring boot . The front end consists of static chat application deployed on Amazon S3.

Here is the complete architecture :

<img width="2246" height="759" alt="diagram-export-8-26-2025-1_05_32-PM" src="https://github.com/user-attachments/assets/dc6b6fe4-cd1e-4bc7-946a-81966e53f0e8" />

## Step 1: Create S3 Bucket for Knowledge Base Documents

Navigate to S3 Console

Go to AWS Console → S3
Click "Create bucket"

Configure Bucket

Bucket name: manufacturing-chatbot-kb-docs
Region: us-east-1 (or your preferred region)
Keep default settings for Block Public Access
Click "Create bucket"

manufacturing-chatbot-kb-docs/
├── product-manuals/
├── troubleshooting/
├── warranty/
├── parts-catalog/
├── safety/
└── faq/

<img width="1788" height="654" alt="Screenshot 2025-08-25 at 1 10 30 PM" src="https://github.com/user-attachments/assets/6c991b08-6389-4431-a063-799eba517f2d" />

Upload the documents in each of these folders as provided in the code repo.

## Step 2: Create Bedrock Knowledge Base

Navigate to Bedrock Console

Go to AWS Console → Amazon Bedrock
Click "Knowledge bases" in left sidebar
Click "Create knowledge base"


Knowledge Base Details

Name: Manufacturing-Support-KB
Description: Knowledge base for manufacturing customer support
IAM Role: Create new service role AmazonBedrockExecutionRoleForKnowledgeBase_manufacturing


Data Source Configuration

Data source name: manufacturing-docs
S3 bucket: s3://manufacturing-chatbot-kb-docs/
Chunking strategy: Default chunking
Keep default preprocessing settings


Embeddings Model

Select: Titan Embeddings G1 - Text
Vector Store : Select Quickly create a new Vector Store -> OpenSearch Serverless
Click Create 

<img width="1478" height="231" alt="Screenshot 2025-08-25 at 1 19 34 PM" src="https://github.com/user-attachments/assets/12336f25-b7b2-4f25-b601-f0f350d7408c" />

## Step 3: Create Bedrock Agent

Navigate to Agents

In Bedrock console, click "Agents" in sidebar
Click "Create Agent"


Agent Details

Agent name: Manufacturing-Support-Agent
Description: AI agent for manufacturing customer support
User input: Enabled
Session timeout: 1 hour


Foundation Model

Select: Anthropic Claude 3 Sonnet

Instructions
You are an expert customer support agent for AcmeMfg Industries, a manufacturing company. 
Your role is to help customers with:

1. Product information and technical specifications
2. Troubleshooting and technical support  
3. Warranty claims and status verification
4. Order tracking and parts identification
5. Safety guidelines and compliance

Always be professional, accurate, and helpful. Use your available tools and knowledge base 
to provide comprehensive answers. If you cannot resolve an issue completely, offer to 
create a support ticket or escalate to human support.

When providing technical information, be specific and include relevant model numbers, 
part numbers, and step-by-step instructions when applicable.

Click "Create"

<img width="1485" height="182" alt="Screenshot 2025-08-25 at 3 08 42 PM" src="https://github.com/user-attachments/assets/b0dbc673-66dc-422d-864a-6bf84a161944" />

<img width="1080" height="500" alt="Screenshot 2025-08-25 at 3 08 49 PM" src="https://github.com/user-attachments/assets/0ca24603-e9f9-47ac-8d8a-cfabd4e68d4e" />


## Step 4: Create Lambda Functions

Navigate to Lambda Console

Go to AWS Console → Lambda
Click "Create function"


Function Configuration

Function name: manufacturing-chatbot-actions
Runtime: Java 17
Architecture: x86_64
Execution role: Create new role with basic Lambda permissions


Add Permissions to Lambda Role

Go to IAM Console
Find the Lambda execution role
Attach policies:

AmazonBedrockFullAccess
AmazonS3ReadOnlyAccess


<img width="1505" height="376" alt="Screenshot 2025-08-25 at 3 11 27 PM" src="https://github.com/user-attachments/assets/9d1973f1-e081-4774-81de-2862d861dae5" />



## Step 5: Create Action Groups for Agent

Go back to your Bedrock Agent

Select your agent → Edit


Add Action Group

Click "Add Action group"
Action group name: ProductActions
Description: Actions for product information and support


Lambda Function

Select: manufacturing-chatbot-actions

Action Group Schema : upload the agent-schema-json JSON file as shown in screenshot ( Full file given in code repo)

<img width="1048" height="886" alt="Screenshot 2025-08-25 at 3 12 30 PM" src="https://github.com/user-attachments/assets/8ad021dd-08d3-40c2-8e01-86984f593409" />


<img width="1078" height="868" alt="Screenshot 2025-08-25 at 3 12 16 PM" src="https://github.com/user-attachments/assets/ae3800d8-3081-46ea-bcdb-4accf9dbe9bd" />

<img width="1035" height="203" alt="Screenshot 2025-08-25 at 3 12 56 PM" src="https://github.com/user-attachments/assets/50b3ffe5-c370-4f35-8d93-fdd23c5438cf" />

Associate Knowledge Base

In agent settings, click "Add knowledge base"
Select your created knowledge base: Manufacturing-Support-KB
Instructions for knowledge base: "Use this knowledge base to answer questions about products, troubleshooting, and support procedures"

<img width="1034" height="194" alt="Screenshot 2025-08-25 at 3 13 00 PM" src="https://github.com/user-attachments/assets/b279702d-6276-494a-8dde-e2cc56c351b3" />

## Step 6: Create Agent Alias

Create Alias

In your agent, click "Create alias"
Alias name: PROD
Description: Production alias for manufacturing support agent


Prepare Agent

## Step 7 : Lambda Function Code Implementation

Code Provided in repo. 
Upload to Lambda

Go to Lambda Console
Select your function: manufacturing-chatbot-actions
Click "Upload from" → ".zip or .jar file"
Upload the JAR file from target/chatbot-lambda-1.0.0.jar


Update Function Configuration

Runtime settings:

Handler: com.manufacturing.chatbot.handler.ChatbotLambdaHandler::handleRequest


Environment variables:

AWS_REGION: us-east-1
SPRING_PROFILES_ACTIVE: lambda

## Step 8: Test Lambda Function

Create test event in Lambda console:

{
  "actionGroup": "ProductActions",
  "apiPath": "/product-specs",
  "httpMethod": "POST",
  "requestBody": {
    "content": {
      "application/json": {
        "body": "{\"productModel\": \"XM-2000\", \"specificationType\": \"technical\"}"
      }
    }
  }
}

## Step 9 : Knowledge Base Documents Upload to S3

Upload the provided Knowledge base documents to s3.

## Step 10 : Test Bedrock Agent 

<img width="369" height="591" alt="Screenshot 2025-08-25 at 4 01 58 PM" src="https://github.com/user-attachments/assets/bdbb2ad5-2e9f-4875-a0f0-f5e5bbaafdaa" />

## Step 11 : Create a Frontend

Create a S3 bucket and upload the index.html file in the bucket. Enable static website hosting on the bucket.

<img width="1493" height="390" alt="Screenshot 2025-08-25 at 4 07 50 PM" src="https://github.com/user-attachments/assets/c758cdad-faa9-4d3d-9d48-8655075b8276" />

index.html is available in the code repo

## Step 12 : Create API Gateway

Phase 5: API Gateway Integration
Step 12: Create API Gateway

Navigate to API Gateway Console

Go to AWS Console → API Gateway
Click "Create API"
Choose "REST API" → "Build"


API Configuration

API name: Manufacturing-Chatbot-API
Description: API for manufacturing customer support chatbot
Endpoint Type: Regional


Create Resource

Click "Actions" → "Create Resource"
Resource Name: chat
Resource Path: /chat
Enable CORS: Yes


Create Method

Select /chat resource
Click "Actions" → "Create Method"
Method: POST
Integration type: Lambda Function
Lambda Function: chat-function

<img width="1386" height="100" alt="Screenshot 2025-08-25 at 4 10 07 PM" src="https://github.com/user-attachments/assets/c7e8c1cf-9cdc-41aa-8197-5b0a353c1f72" />

## Step 13 : Lambda Function to invoke Bedrock Agent

Create a Lambda function chat-function . Upload the given python file to the code. Build and Deploy.

## Replace API Gateway path in the index.html in S3.

## Step 14 : Test the AI application

<img width="1151" height="772" alt="Screenshot 2025-08-25 at 4 14 11 PM" src="https://github.com/user-attachments/assets/ef5399de-740c-48f0-816f-2c6f55efc69b" />

## Step 15 (Optional ) : Map it to the domain (using Cloudfront)

if you own a domain and want to run the application using your domain. We need to procure certificate using AWS ACM, Create a cloud front distribution.
Add a entry into Route 53 hosted Zone. By following these we will be able to run AI application in our owned domain.

<img width="1180" height="949" alt="Screenshot 2025-08-25 at 4 16 02 PM" src="https://github.com/user-attachments/assets/88136ea9-4786-429d-93db-a765148fe41f" />


# Congratulations , We have now successfully configured AI Application for customer support of a manufacturing Company using Agentic RAG powered by Amazon Bedrock



