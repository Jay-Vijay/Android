import { View, Image, StyleSheet, Text } from "react-native";
import Welcome from "./components/Welcome";
import Footer from "./components/Footer";
import SendButton from "./components/SendButton";
import ImageButtons from "./components/ImageButtons";
import { useState } from "react";

export default function App() {
  const [image, setImage] = useState(null);
  const [prediction, setPrediction] = useState(null);
  console.log(image);

  const renderPrediction = () => {
    if (!prediction) return null;

    const { diagnosis, probabilities, warning } = prediction;

    // Render diagnosis and probabilities
    return (
      <View>
        <Text>{diagnosis}</Text>
        {probabilities && Object.entries(probabilities).map(([label, probability]) => (
          <Text key={label}>{`${label}: ${probability}`}</Text>
        ))}
        <Text>{warning}</Text>
      </View>
    );
  };

  return (
    <View style={{ flex: 1 }}>
      <View style={{ justifyContent: "space-around" }}>
        {image ? (
          <View>
            <SendButton image={image} prediction={prediction} setPrediction={setPrediction} />
            {renderPrediction() || <Image source={{ uri: image }} style={styles.image} />}
          </View>
        ) : (
          <Welcome />
        )}
        <ImageButtons image={image} setImage={setImage} />
      </View>
      <Footer />
    </View>
  );
}

const styles = StyleSheet.create({
  image: {
    marginTop: 200,
    marginLeft: 6,
    width: 400,
    height: 400,
  },
});
