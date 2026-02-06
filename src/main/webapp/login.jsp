<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
  <div class="card shadow p-4" style="max-width: 420px; margin:auto;">
    <h3 class="mb-3">Connexion</h3>

    <div class="mt-3 text-center">
      <a href="Register">Créer un compte</a>
    </div>

    <% String err = (String) request.getAttribute("error"); %>
    <% if (err != null) { %>
      <div class="alert alert-danger"><%= err %></div>
    <% } %>

    <form method="post" action="Login">
      <div class="mb-3">
        <label class="form-label">Username</label>
        <input class="form-control" name="username" required>
      </div>

      <div class="mb-3">
        <label class="form-label">Password</label>
        <input class="form-control" type="password" name="password" required>
      </div>

      <button class="btn btn-primary w-100">Se connecter</button>
    </form>
  </div>
</div>
</body>
</html>