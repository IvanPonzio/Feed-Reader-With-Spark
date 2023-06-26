# # Java Feed Reader with Spark

This is a console-based feed reader application implemented in Java, which allows users to automatically retrieve and display feeds from various websites. The application follows the object-oriented programming paradigm and utilizes a JSON file to specify the different sites and their corresponding topics from which to obtain the feeds. Additionally, the application integrates with the Spark framework for enhanced performance and scalability.

## Features

1. **Feed Retrieval**: The application retrieves feeds from different websites based on the information provided in a JSON configuration file.
2. **User-friendly Display**: The retrieved feeds are presented in a readable and user-friendly format on the console.
3. **Named Entity Recognition**: The application incorporates a heuristic algorithm to compute the most mentioned named entities within the list of feeds.
4. **Spark Integration**: The application leverages Spark framework to enhance performance and scalability.

## Getting Started

To use this feed reader, follow the instructions below:

1. Clone the repository:

   ```bash
   git clone https://github.com/IvanPonzio/FeedReaderWithSpark.git
   ```

2. Make sure you have Java Development Kit (JDK) installed on your system.

3. Install Spark framework. You can download it from the official Spark website: [spark.apache.org](https://spark.apache.org/).

4. Import the project into your preferred Java IDE.

5. Set up the JSON configuration file with the desired websites and topics. An example configuration file (`config.json`) is provided in the project's root directory. The configuration should be in the following format:

   ```json
   {
     "sites": [
       {
         "url": "https://rss.nytimes.com/services/xml/rss/nyt/%s.xml",
         "urlParams": ["Business", "Technology", "Sports"],
         "urlType": "rss"
       },
       // Add more sites and their topics here
     ]
   }
   ```

   - `url`: The URL pattern for the website's feed. Use `%s` as a placeholder for the topic.
   - `urlParams`: An array of topics to replace the `%s` placeholder in the URL pattern.
   - `urlType`: The type of feed URL (e.g., RSS, XML, JSON, etc.).

6. Build and run the application.

7. The application will automatically retrieve feeds from the specified websites and topics, displaying them on the console.

## Contributing

Contributions to this feed reader are welcome! If you find any bugs or have suggestions for improvements, please feel free to open an issue or submit a pull request on the GitHub repository.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

This feed reader application was developed as part of a laboratory project at the Faculty of Mathematics, Astronomy, Physics, and Computer Science (FAMAF). We would like to acknowledge the support and guidance provided by our instructors and the resources available online that helped in building this application.

If you have any questions or need further assistance, please feel free to contact us.

Happy reading!
