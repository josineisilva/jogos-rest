<?php
$url = str_replace("\n","",file_get_contents("url.txt"));
$url = $url.'/screenshotcomentario';
$data = array(
  "screenshot_id" => (int)$argv[1],
  "texto" => "Teste de comentario de screnshot",
);
$postdata = json_encode($data);
$authorization = "Authorization: Bearer ".str_replace("\n","",file_get_contents("jwt.txt"));
$curl = curl_init($url);
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, $postdata);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type: application/json' , $authorization ));
$result = curl_exec($curl);
$httpcode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
curl_close($curl);
echo 'HTTP code: ' . $httpcode;
echo "\n";
$json =json_decode($result);
print_r(json_encode($json,JSON_PRETTY_PRINT));
echo "\n";
?>
