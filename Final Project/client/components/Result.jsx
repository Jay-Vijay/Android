import React from 'react';
import { View, Image, StyleSheet, Text, Button } from "react-native";

function Result({ route, navigation }) {
  const { image, prediction } = route.params;

  const renderPrediction = () => {
    if (!prediction) return null;

    const { diagnosis, probabilities, warning } = prediction;

    return (
      <View style={styles.predictionContainer}>
        <Text style={styles.diagnosis}>{diagnosis}</Text>
        {probabilities && Object.entries(probabilities).map(([label, probability]) => (
          <Text key={label} style={styles.probability}>{`${label}: ${probability}`}</Text>
        ))}
        <Text style={styles.warning}>{warning}</Text>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <Image source={{ uri: image }} style={styles.image} />
      {renderPrediction()}
      <Button title="Go Back" onPress={() => navigation.goBack()} />
    </View>
  );
}

export default Result;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    alignItems: "center",
    justifyContent: "center",
  },
  image: {
    width: 300,
    height: 300,
    marginBottom: 20,
    borderRadius: 10,
  },
  predictionContainer: {
    alignItems: "center",
    marginBottom: 20,
  },
  diagnosis: {
    fontSize: 20,
    fontWeight: "bold",
    marginBottom: 10,
  },
  probability: {
    fontSize: 16,
    marginBottom: 5,
  },
  warning: {
    fontSize: 16,
    color: "red",
    marginTop: 10,
  },
});
