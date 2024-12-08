# Deployment Guide: Deploying Backend API with Google App Engine

This guide walks you through deploying a Backend API on Google Cloud Platform (GCP) using App Engine. Follow these steps to successfully set up and deploy your application.

## Prerequisites
Before starting, ensure you have the following:

1. **Google Cloud Platform (GCP) Account**
   - If you don't have one, create an account at [GCP Console](https://console.cloud.google.com/).

2. **GCP Project with Billing Enabled**
   - Create a new project or use an existing one.
   - Ensure billing is enabled for the project.

## Step 1: Creating an App Engine Application
1. Navigate to the **App Engine** section of the GCP Console.
2. Click **Create Application**.
3. Select a region closest to your location for optimal performance.
4. Choose the **App Engine default service account** for "Identity and API Access."
5. Click **Next** and wait for the App Engine application to be created.

## Step 2: Granting Permissions
To ensure your application works properly, give the App Engine default service account access to the required roles:
1. Navigate to **IAM & Admin** in the GCP Console.
2. Locate the App Engine service account (usually named something like `YOUR_PROJECT_ID@appspot.gserviceaccount.com`).
3. Assign the **Storage Admin** role to the service account.

## Step 3: Setting Up Your Backend API
1. Open **Cloud Shell** in the GCP Console.
2. Clone your repository by running the following command:
   ```bash
   git clone <repository-url>
   ```
3. Navigate into the project directory:
   ```bash
   cd <project-directory>
   ```
4. Install the required Python dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Step 4: Deploying Your Backend API
1. Deploy the application to App Engine using the `gcloud` CLI:
   ```bash
   gcloud app deploy
   ```
2. Confirm the deployment when prompted and wait for the process to complete.

## Step 5: Testing Your Deployed Application
1. After deployment, open the application in your browser by running:
   ```bash
   gcloud app browse
   ```
2. The URL displayed is your live backend API.
3. You can now use the `/predict` endpoint in your application by making API calls to the provided URL.

## Notes
- Make sure all necessary dependencies are included in your project.
- If you encounter issues during deployment, check the App Engine logs in the GCP Console for debugging.
- Secure your endpoints by implementing authentication if needed.

## Useful Links
- [GCP Console](https://console.cloud.google.com/)
- [App Engine Documentation](https://cloud.google.com/appengine/docs)
- [Cloud Shell Guide](https://cloud.google.com/shell/docs)

---

