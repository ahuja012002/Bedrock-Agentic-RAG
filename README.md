# Bedrock-Agentic-RAG

Agentic Retrieval Augmented Generation (RAG) applications represent an advanced approach in AI that integrates foundation models (FMs) with external knowledge retrieval and autonomous agent capabilities. These systems dynamically access and process information, break down complex tasks, use external tools, apply reasoning, and adapt to various contexts. They go beyond simple question answering by performing multi-step processes, making decisions, and generating complex outputs.

In this example, we will build an agentic RAG application for a customer support of a manufacturing Company. We have used Bedrock Knowledge Base. Agents in Bedrock backed by Action groups Lambda function . The Agents are coded in Java, Spring boot . The front end consists of static chat application deployed on Amazon S3.

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

Click "Prepare" to compile the agent
Wait for preparation to complete
