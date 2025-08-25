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
