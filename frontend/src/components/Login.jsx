import { useState } from 'react';
import { loginDoctor, registerDoctor } from '../hooks/usePatientApi';
import logo from '../assets/hospital-red-norte-logo.svg';
import './Login.css';

const EMAIL_DOMAIN = '@redsalud.cl';

function Login({ onLogin, error: externalError }) {
  const [mode, setMode] = useState('login');
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  });
  const [registerData, setRegisterData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [registeredUsers, setRegisteredUsers] = useState(1);

  const handleLoginSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setSuccess('');

    const email = credentials.email.trim().toLowerCase();

    if (!email.endsWith(EMAIL_DOMAIN)) {
      setError('Usa tu correo institucional @redsalud.cl.');
      return;
    }

    try {
      const auth = await loginDoctor({
        email,
        password: credentials.password
      });
      setRegisteredUsers(auth.registeredUsers);
      onLogin({ name: auth.name, email: auth.email, role: auth.role, token: auth.token });
    } catch (e) {
      setError('Cuenta no encontrada, credenciales incorrectas o BFF no disponible.');
    }
  };

  const handleRegisterSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setSuccess('');

    const email = registerData.email.trim().toLowerCase();
    const name = registerData.name.trim();

    if (!name) {
      setError('Ingresa el nombre del medico.');
      return;
    }

    if (!email.endsWith(EMAIL_DOMAIN)) {
      setError('Solo se permite registrar correos @redsalud.cl.');
      return;
    }

    if (registerData.password.length < 8) {
      setError('La contrasena debe tener al menos 8 caracteres.');
      return;
    }

    if (registerData.password !== registerData.confirmPassword) {
      setError('Las contrasenas no coinciden.');
      return;
    }

    try {
      const auth = await registerDoctor({ name, email, password: registerData.password });
      setRegisteredUsers(auth.registeredUsers);
      setCredentials({ email, password: '' });
      setRegisterData({ name: '', email: '', password: '', confirmPassword: '' });
      setSuccess('Registro protegido creado. Ahora inicia sesion.');
      setMode('login');
    } catch (e) {
      setError('No se pudo registrar. El correo puede existir o el BFF no esta disponible.');
    }
  };

  return (
    <main className="auth-shell">
      <section className="auth-brand-panel">
        <img src={logo} alt="Hospital Red Norte" className="auth-logo" />
        <div>
          <span className="eyebrow">Gestion privada RedSalud</span>
          <h1>Panel clinico Hospital Red Norte</h1>
          <p>
            Acceso exclusivo para medicos registrados con correo institucional.
          </p>
        </div>
        <div className="auth-summary">
          <article>
            <strong>{registeredUsers}</strong>
            <span>medicos registrados</span>
          </article>
          <article>
            <strong>5</strong>
            <span>servicios integrados</span>
          </article>
        </div>
      </section>

      <section className="auth-card">
        <div className="auth-tabs" aria-label="Acceso medico">
          <button className={mode === 'login' ? 'active' : ''} type="button" onClick={() => setMode('login')}>
            Iniciar sesion
          </button>
          <button className={mode === 'register' ? 'active' : ''} type="button" onClick={() => setMode('register')}>
            Registrarse
          </button>
        </div>

        {mode === 'login' ? (
          <form onSubmit={handleLoginSubmit} className="login-form">
            <div>
              <span className="eyebrow">Acceso medico</span>
              <h2>Iniciar sesion</h2>
            </div>
            <label className="form-group">
              <span>Correo institucional</span>
              <input
                type="email"
                value={credentials.email}
                onChange={(event) => setCredentials({ ...credentials, email: event.target.value })}
                placeholder="nombre@redsalud.cl"
                required
              />
            </label>
            <label className="form-group">
              <span>Contrasena</span>
              <input
                type="password"
                value={credentials.password}
                onChange={(event) => setCredentials({ ...credentials, password: event.target.value })}
                placeholder="Ingresa tu contrasena"
                required
              />
            </label>
            {(error || externalError || success) && (
              <div className={success ? 'success-message' : 'error-message'}>{success || error || externalError}</div>
            )}
            <button type="submit" className="login-button">Entrar al panel</button>
          </form>
        ) : (
          <form onSubmit={handleRegisterSubmit} className="login-form">
            <div>
              <span className="eyebrow">Nuevo medico</span>
              <h2>Crear cuenta</h2>
            </div>
            <label className="form-group">
              <span>Nombre completo</span>
              <input
                value={registerData.name}
                onChange={(event) => setRegisterData({ ...registerData, name: event.target.value })}
                placeholder="Dra. Nombre Apellido"
                required
              />
            </label>
            <label className="form-group">
              <span>Correo institucional</span>
              <input
                type="email"
                value={registerData.email}
                onChange={(event) => setRegisterData({ ...registerData, email: event.target.value })}
                placeholder="nombre@redsalud.cl"
                required
              />
            </label>
            <div className="auth-form-grid">
              <label className="form-group">
                <span>Contrasena</span>
                <input
                  type="password"
                  value={registerData.password}
                  onChange={(event) => setRegisterData({ ...registerData, password: event.target.value })}
                  required
                />
              </label>
              <label className="form-group">
                <span>Confirmar</span>
                <input
                  type="password"
                  value={registerData.confirmPassword}
                  onChange={(event) => setRegisterData({ ...registerData, confirmPassword: event.target.value })}
                  required
                />
              </label>
            </div>
            {error && <div className="error-message">{error}</div>}
            <button type="submit" className="login-button">Registrar medico</button>
          </form>
        )}
      </section>
    </main>
  );
}

export default Login;
