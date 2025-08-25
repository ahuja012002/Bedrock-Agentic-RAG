import json
import boto3
import uuid
import logging
from botocore.exceptions import ClientError

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    # Check if this is a streaming request - handle None queryStringParameters
    query_params = event.get('queryStringParameters') or {}
    is_streaming = query_params.get('stream', 'false').lower() == 'true'
    
    cors_headers = {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Headers': 'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token',
        'Access-Control-Allow-Methods': 'OPTIONS,POST,GET'
    }
    
    if is_streaming:
        cors_headers['Content-Type'] = 'text/event-stream'
        cors_headers['Cache-Control'] = 'no-cache'
        cors_headers['Connection'] = 'keep-alive'
    else:
        cors_headers['Content-Type'] = 'application/json'
    
    try:
        # Handle preflight OPTIONS request
        if event.get('httpMethod') == 'OPTIONS':
            return {
                'statusCode': 200,
                'headers': cors_headers,
                'body': json.dumps({'message': 'CORS preflight successful'})
            }
        
        # Parse request body
        body = json.loads(event.get('body', '{}'))
        user_message = body.get('message', '').strip()
        session_id = body.get('sessionId', str(uuid.uuid4()))
        agent_id = body.get('agentId', 'FTRWILFTLR')
        agent_alias_id = body.get('agentAliasId', '9LRQLTRYDD')  # Your actual alias ID
        
        # Add debugging logs
        logger.info(f"Agent ID: {agent_id}")
        logger.info(f"Agent Alias ID: {agent_alias_id}")
        logger.info(f"Session ID: {session_id}")
        logger.info(f"User Message: {user_message}")
        
        if not user_message:
            raise ValueError("Message cannot be empty")
        
        # Initialize Bedrock client with explicit region and timeout
        bedrock_runtime = boto3.client(
            'bedrock-agent-runtime', 
            region_name='us-east-1',
            config=boto3.session.Config(
                read_timeout=300,  # 5 minutes
                connect_timeout=60,  # 1 minute
                retries={'max_attempts': 2}
            )
        )
        
        # Invoke the Bedrock agent
        logger.info(f"Invoking Bedrock agent...")
        response = bedrock_runtime.invoke_agent(
            agentId=agent_id,
            agentAliasId=agent_alias_id,
            sessionId=session_id,
            inputText=user_message
        )
        logger.info(f"Bedrock agent invoked successfully")
        
        if is_streaming:
            # For streaming response (SSE format)
            sse_response = ""
            full_response = ""
            
            for event_chunk in response.get("completion", []):
                chunk = event_chunk.get("chunk", {})
                if "bytes" in chunk:
                    chunk_text = chunk["bytes"].decode('utf-8')
                    full_response += chunk_text
                    
                    # Format as Server-Sent Event
                    sse_response += f"data: {json.dumps({'chunk': chunk_text, 'sessionId': session_id})}\n\n"
            
            # Add final event
            sse_response += f"data: {json.dumps({'done': True, 'sessionId': session_id})}\n\n"
            
            return {
                'statusCode': 200,
                'headers': cors_headers,
                'body': sse_response
            }
        else:
            # For regular JSON response
            full_response = ""
            for event_chunk in response.get("completion", []):
                chunk = event_chunk.get("chunk", {})
                if "bytes" in chunk:
                    full_response += chunk["bytes"].decode('utf-8')
            
            return {
                'statusCode': 200,
                'headers': cors_headers,
                'body': json.dumps({
                    'success': True,
                    'response': full_response,
                    'sessionId': session_id,
                    'timestamp': context.aws_request_id
                })
            }
            
    except Exception as e:
