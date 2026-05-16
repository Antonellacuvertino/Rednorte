import { useState } from 'react';

function LoginForm({ onLogin, error }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    await onLogin({ username, password });
    setLoading(false);
  };

  return (
    <form className="patient-form" onSubmit={handleSubmit}>
      <div className="form-grid">
        <label className="field">
          <span>Usuario</span>
          <input
            name="username"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            placeholder="medico"
            required
          />
        </label>

        <label className="field">
          <span>Contraseña</span>
          <input
            name="password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            placeholder="salud123"
            required
          />
        </label>
      </div>

      {error && <div className="notice error">{error}</div>}

      <button className="primary-button" type="submit" disabled={loading}>
        {loading ? 'Validando...' : 'Ingresar como médico'}
      </button>
    </form>
  );
}

export default LoginForm;
