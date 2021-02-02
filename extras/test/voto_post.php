<?php
$url = str_replace("\n","",file_get_contents("url.txt"));
$url = $url.'/voto/'.(string)$argv[1];
$authorization = "Authorization: Bearer ".str_replace("\n","",file_get_contents("jwt.txt"));
$curl = curl_init($url);
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_HTTPHEADER, array($authorization ));
$result = curl_exec($curl);
$httpcode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
curl_close($curl);
echo 'HTTP code: ' . $httpcode;
echo "\n";
$json =json_decode($result);
print_r(json_encode($json,JSON_PRETTY_PRINT));
echo "\n";
?>
