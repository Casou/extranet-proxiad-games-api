function Get-UrlStatusCode()
{
	$pathFile = "C:\Temp\testPowershell.txt"

	if (Test-Path $pathFile) {
		$file = new-object System.IO.StreamReader($pathFile)
		$url = $file.ReadLine().split(':', 2)[1]
		$salle = $file.ReadLine().split(':')[1]
		$file.close()

		$url += "?salle=" + $salle

		try {
			(Invoke-WebRequest -Uri $url -UseBasicParsing -DisableKeepAlive -Method head).StatusCode

			write-host ""
			write-host "  Traitement en cours. Merci de patienter. ..."
			write-host ""
			Read-Host
		} catch [Net.WebException] {
			[int]$_.Exception.Response.StatusCode
			write-host ""
			write-host "  /!\  J'ai besoin d'une connexion Internet  /!\"
			write-host ""
			Read-Host
		}
	} else {
		write-host ""
		write-host "  /!\  Cet ordinateur ne dispose pas"
		write-host "       de la configuration necessaire"
		write-host "       a mon execution                 /!\"
		write-host ""
		Read-Host
	}
}

$statusCode = Get-UrlStatusCode