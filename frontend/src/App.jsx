import { useEffect, useState } from 'react'
import './App.css'
import TransactionList from './components/TransactionList'
import { depositToMyWallet,
  getMyTransactions,
  getMyWallet,
  login,
  register,
  transferFromMyWallet, } from './services/walletApi'
import DepositForm from './components/DepositForm'
import TransferForm from './components/TransferForm'
import WalletSummary from './components/WalletSummary'
import LoginForm from './components/LoginForm'
import RegisterForm from './components/RegisterForm'


function App() {
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [wallet, setWallet] = useState(null)
  const [authMode, setAuthMode] = useState('login')
  
  const [token, setToken] = useState(() => localStorage.getItem('token') || '')
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('user')
    return storedUser ? JSON.parse(storedUser) : null
  })



  useEffect(() => {
    loadWalletData()
  }, [])

function loadWalletData(authToken = token) {
  if (!authToken) {
    return
  }

  setLoading(true)
  setError('')

  Promise.all([
    getMyWallet(authToken),
    getMyTransactions(authToken),
  ])
    .then(([walletData, transactionsData]) => {
      setWallet(walletData)
      setTransactions(transactionsData)
    })
    .catch(() => {
      setError('Could not load wallet data.')
    })
    .finally(() => {
      setLoading(false)
    })
}

function handleDeposit(amount) {
  depositToMyWallet(token, amount)
    .then(() => {
      loadWalletData()
    })
    .catch((error) => {
      setError(error.message)
    })
}

function handleTransfer(receiverWalletId, amount) {
  transferFromMyWallet(token, receiverWalletId, amount)
    .then(() => {
      loadWalletData()
    })
    .catch((error) => {
      setError(error.message)
    })
}

function handleLogin(email, password) {
  login(email, password)
    .then((data) => {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data.user))

      setToken(data.token)
      setUser(data.user)
      loadWalletData(data.token)
    })
    .catch((error) => {
      setError(error.message)
    })
}

function handleRegister(fullName, email, password) {
  register(fullName, email, password)
    .then(() => {
      setAuthMode('login')
      setError('')
      setSuccessMessage('Account created. Please login.')
    })
    .catch((error) => {
      setSuccessMessage('')
      setError(error.message)
    })
}

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  
  setToken('')
  setUser(null)
  setWallet(null)
  setTransactions([])
  setError('')
}

if (!token) {
  return (
    <main className="dashboard">
      <header className="topbar">
        <div>
          <p className="eyebrow">Digital Wallet System</p>
          <h1>{authMode === 'login' ? 'Login' : 'Create account'}</h1>
        </div>
      </header>

      <section className="actions-card auth-card">
        {authMode === 'login' ? (
          <>
            <LoginForm onLogin={handleLogin} />

            <p className="auth-switch">
              Don't have an account?{' '}
              <button type="button" onClick={() => setAuthMode('register')}>
                Create account
              </button>
            </p>
          </>
        ) : (
          <>
            <RegisterForm onRegister={handleRegister} />

            <p className="auth-switch">
              Already have an account?{' '}
              <button type="button" onClick={() => setAuthMode('login')}>
                Login
              </button>
            </p>
          </>
        )}

        {error && <p className="error-message">{error}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}
      </section>
    </main>
  )
}

  return (
  <main className="dashboard">
    <header className="topbar">
      <div>
        <p className="eyebrow">Digital Wallet System</p>
        <h1>Wallet dashboard</h1>
      </div>

      <div className="user-actions">
      <span className="wallet-pill">{user?.email}</span>
      <button className="secondary-button" onClick={handleLogout}>
        Logout
      </button>
    </div>
    </header>

    <div className="dashboard-grid">
      <div className="sidebar">
        <WalletSummary wallet={wallet} />

        <section className="actions-card">
          <DepositForm onDeposit={handleDeposit} />
          <TransferForm onTransfer={handleTransfer} />
        </section>
      </div>

      <section className="activity-card">
        <div className="section-header">
          <div>
            <p className="eyebrow">Activity</p>
            <h2>Recent transactions</h2>
          </div>

          {loading && <span className="muted">Loading...</span>}
        </div>

        {error && <p className="error-message">{error}</p>}

        {!loading && !error && (
          <TransactionList transactions={transactions} />
        )}
      </section>
    </div>
  </main>
)
}

export default App