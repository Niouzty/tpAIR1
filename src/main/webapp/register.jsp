<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Inscription</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
  <div class="card shadow p-4" style="max-width: 520px; margin:auto;">
    <h3 class="mb-3">Créer un compte</h3>

    <% String err = (String) request.getAttribute("error"); %>
    <% if (err != null) { %>
      <div class="alert alert-danger"><%= err %></div>
    <% } %>

    <form method="post" action="Register">
      <div class="mb-3">
        <label class="form-label">Username</label>
        <input class="form-control" name="username" required>
      </div>

      <div class="mb-3">
        <label class="form-label">Email</label>
        <input class="form-control" type="email" name="email" required>
      </div>

      <div class="mb-3">
        <label class="form-label">Password</label>
        <input class="form-control" type="password" name="password" required>
      </div>

      <button class="btn btn-success w-100">S'inscrire</button>
    </form>

    <div class="mt-3 text-center">
      <a href="Login">J'ai déjà un compte</a>
    </div>
  </div>
</div>
</body>
</html>