import { useState } from 'react'

function RegisterForm({ onRegister, error }) {
  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  function handleSubmit(event) {
    event.preventDefault()
    onRegister(fullName, email, password)
  }

  return (
    <section className="auth-card">
      <p className="eyebrow">Digital Wallet System</p>
      <h1>Create account</h1>

      <form onSubmit={handleSubmit} className="form-stack">
        <label>
          Full name
          <input
            type="text"
            placeholder="Enter full name"
            value={fullName}
            onChange={(event) => setFullName(event.target.value)}
            required
          />
        </label>

        <label>
          Email
          <input
            type="email"
            placeholder="Enter email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </label>

        <label>
          Password
          <input
            type="password"
            placeholder="Enter password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </label>

        <button type="submit">Create account</button>
      </form>

      {error && <p className="error-message">{error}</p>}
    </section>
  )
}

export default RegisterForm