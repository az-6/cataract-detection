from google.cloud import storage

storage_client = storage.Client()
BUCKET_NAME = 'cataract-detection'

def upload_to_gcs(file, filename, folder, timeout=60):
    bucket = storage_client.bucket(BUCKET_NAME)
    blob = bucket.blob(f"{folder}/{filename}")
    blob.upload_from_filename(file, timeout=timeout)
    return None