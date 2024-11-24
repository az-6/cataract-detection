from flask import Flask, request, jsonify, render_template
import tensorflow as tf
from flask_cors import CORS
from predict import predict_cataract
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
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})
    
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'})
    
    if file:
        # Create temporary file
        with tempfile.NamedTemporaryFile(delete=False) as temp_file:
            file.save(temp_file.name)
            result = predict_cataract(model, temp_file.name)
            os.unlink(temp_file.name)  # Delete the temporary file
            return jsonify({'result': float(result)})

if __name__ == '__main__':
    os.makedirs('tmp', exist_ok=True)
    app.run(debug=True)
