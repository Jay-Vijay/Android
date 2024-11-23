import { View, StyleSheet, TouchableOpacity, Text } from "react-native";

function SendButton({ image, setPrediction, navigation }) {
  const formData = new FormData();
  formData.append('image', {
    uri: image,
    type: 'image/jpeg',
    name: 'test.jpg',
  });

  // Simulated mock response
  const mockResponse = {
    diagnosis: "You most likely have Melanocytic-nevi",
    probabilities: {
      "Melanocytic-nevi": "90%",
      "Melanoma": "6%",
      "Benign-keratosis-like-lesions": "1%",
      "Actinic-keratoses": "1%",
      "Vascular-lesions": "1%",
    },
    warning: "Warning: Please consult a doctor for an accurate diagnosis.",
  };

  const sendToServer = async () => {
    // Simulating server response and navigating to Result
    setPrediction(mockResponse);
    navigation.navigate("Result", { image, prediction: mockResponse });

    // Uncomment below for actual API call
    // try {
    //   const response = await axios.post("http://192.168.1.6:8000/predict", formData);
    //   setPrediction(response.data);
    //   navigation.navigate("Result", { image, prediction: response.data });
    // } catch (error) {
    //   console.error("Error sending image:", error);
    // }
  };

  return (
    <View style={styles.sendWrapper}>
      <TouchableOpacity style={styles.button} onPress={sendToServer}>
        <Text style={styles.buttonText}>Scan the Image</Text>
      </TouchableOpacity>
    </View>
  );
}

export default SendButton;

const styles = StyleSheet.create({
  sendWrapper: {
    position: "absolute",
    top: "10%",
    left: "50%",
    transform: [{ translateX: -130 }, { translateY: 20 }],
  },
  button: {
    backgroundColor: "#4a90e2",
    padding: 30,
    borderRadius: 5,
    marginRight: 10,
    display: "flex",
    justifyContent: "center",
  },
  buttonText: {
    color: "white",
    marginRight: 5,
    fontSize: 20,
  },
});
