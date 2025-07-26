#!/bin/bash

# Thermos Bottle Management - Setup Script

echo "🌡️  Thermos Bottle Management Setup"
echo "===================================="
echo ""

# Create sample Firebase config if not exists
if [ ! -f "src/main/resources/firebase-service-account-key.json" ]; then
    echo "📁 Creating sample Firebase config..."
    mkdir -p src/main/resources
    cat > src/main/resources/firebase-service-account-key-sample.json << 'EOF'
{
  "type": "service_account",
  "project_id": "your-project-id",
  "private_key_id": "your-private-key-id",
  "private_key": "-----BEGIN PRIVATE KEY-----\nYOUR_PRIVATE_KEY\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-xxxxx@your-project-id.iam.gserviceaccount.com",
  "client_id": "your-client-id",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-xxxxx%40your-project-id.iam.gserviceaccount.com"
}
EOF
    echo "   ✅ Created firebase-service-account-key-sample.json"
    echo "   📝 Please rename it to firebase-service-account-key.json and update with your Firebase credentials"
fi

# Create application-local.properties sample
if [ ! -f "src/main/resources/application-local.properties" ]; then
    echo "📁 Creating local application config sample..."
    cat > src/main/resources/application-local.properties << 'EOF'
# Local development configuration
# Copy this to application.properties and update the values

# OAuth2 Google Configuration - REPLACE WITH YOUR VALUES
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID_HERE
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET_HERE

# Firebase configuration - REPLACE WITH YOUR PROJECT ID
firebase.database-url=https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com

# Server configuration (optional)
server.port=8080

# Debug mode (optional)
logging.level.com.example.thermos=DEBUG
EOF
    echo "   ✅ Created application-local.properties sample"
fi

echo ""
echo "📋 Setup Instructions:"
echo "----------------------"
echo ""
echo "1. 🔑 Google OAuth2 Setup:"
echo "   • Go to https://console.cloud.google.com/"
echo "   • Create a new project or select existing"
echo "   • Enable Google+ API"
echo "   • Create OAuth 2.0 Client ID (Web application)"
echo "   • Add redirect URI: http://localhost:8080/login/oauth2/code/google"
echo "   • Copy Client ID and Client Secret to application.properties"
echo ""
echo "2. 🔥 Firebase Setup:"
echo "   • Go to https://console.firebase.google.com/"
echo "   • Create a new project"
echo "   • Enable Firestore Database"
echo "   • Go to Project Settings → Service Accounts"
echo "   • Generate new private key"
echo "   • Download JSON file and rename to firebase-service-account-key.json"
echo "   • Place in src/main/resources/"
echo ""
echo "3. ⚙️  Application Configuration:"
echo "   • Copy application-local.properties to application.properties"
echo "   • Update Google Client ID and Secret"
echo "   • Update Firebase project ID"
echo ""
echo "4. 🚀 Run the application:"
echo "   • ./start.sh"
echo "   • Or: mvn spring-boot:run"
echo ""
echo "📞 Need help? Check the README.md file for detailed instructions."
echo ""
echo "✨ Happy coding!"