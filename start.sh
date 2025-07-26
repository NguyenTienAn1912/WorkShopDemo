#!/bin/bash

# Thermos Bottle Management - Startup Script

echo "🌡️  Starting Thermos Bottle Management System..."
echo "================================================"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f 2 | cut -d'.' -f 1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check if Firebase config exists
if [ ! -f "src/main/resources/firebase-service-account-key.json" ]; then
    echo "⚠️  Warning: Firebase service account key not found!"
    echo "   Please add firebase-service-account-key.json to src/main/resources/"
    echo "   The application will start but Firebase features may not work."
fi

# Check if application.properties is configured
if grep -q "your-google-client-id" src/main/resources/application.properties; then
    echo "⚠️  Warning: Google OAuth2 not configured!"
    echo "   Please update your Google Client ID and Secret in application.properties"
fi

echo ""
echo "🔧 Building application..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo "✅ Build successful!"
echo ""
echo "🚀 Starting Spring Boot application..."
echo "   Application will be available at: http://localhost:8080"
echo ""
echo "📋 Features available:"
echo "   • Google OAuth2 Login"
echo "   • CRUD operations with Firebase"
echo "   • Responsive web interface"
echo ""
echo "Press Ctrl+C to stop the application"
echo "================================================"

# Start the application
mvn spring-boot:run