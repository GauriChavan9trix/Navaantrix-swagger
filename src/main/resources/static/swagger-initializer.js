window.onload = function () {
  const urlParams = new URLSearchParams(window.location.search);
  const yamlUrl = urlParams.get("url");

  if (!yamlUrl) {
    document.getElementById("swagger-ui").innerHTML =
      "<p style='color:red;'>Missing URL parameter</p>";
    return;
  }

  console.log("Loading from:", yamlUrl); // ✅ Confirm what it's trying to fetch

  window.ui = SwaggerUIBundle({
    url: yamlUrl,
	configUrl: null, // ✅ disable swagger-config
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });
};
