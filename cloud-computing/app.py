from flask import Flask, request, jsonify
import tensorflow as tf
from flask_cors import CORS
from predict import predict_cataract
from bucket import upload_to_gcs
import os
import tempfile

app = Flask(__name__)
CORS(app)

MODEL_PATH = os.path.join(os.path.dirname(__file__), 'cataract_model.h5')
model = tf.keras.models.load_model(MODEL_PATH)

@app.route('/')
def home():
    return "Welcome to the prediction API. Use the /predict endpoint for predictions."

@app.route('/predict', methods=['POST'])
def predict():
    if 'files' not in request.files:
        return jsonify({'error': 'No file part'})
    
    uploaded_files = request.files.getlist('files')
    if len(uploaded_files) == 0:
        return jsonify({'error': 'No selected files'})
    
    with tempfile.TemporaryDirectory() as temp_dir:
        image_paths = []
        for file in uploaded_files:
            file_path = os.path.join(temp_dir, file.filename)
            file.save(file_path)
            image_paths.append(file_path)

        results = predict_cataract(image_paths, model)

        for file_path, result in zip(image_paths, results):
            folder = result["condition"]
            upload_to_gcs(file_path, os.path.basename(file_path), folder, timeout=120)

        return jsonify({'results': results})

if __name__ == '__main__':
    os.makedirs('tmp', exist_ok=True)
    app.run(host='0.0.0.0', port=8080, debug=True)
