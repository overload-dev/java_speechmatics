<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h1>Speechmatics API Sample</h1>
	<div>
		<h3>send your audio file to Speechmatics</h3>
		<form method="post" action="/upload" enctype="multipart/form-data">
			<input type="file" name="audio"> <input type="submit"
				value="submit">
		</form>
	</div>
</body>
</html>
