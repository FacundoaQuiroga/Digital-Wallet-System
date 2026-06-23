import { useEffect, useState } from 'react'
import './App.css'
import TransactionList from './components/TransactionList'
import { depositToWallet,getWallet,getWalletTransactions,transferMoney } from './services/walletApi'
import DepositForm from './components/DepositForm'
import TransferForm from './components/TransferForm'
import WalletSummary from './components/WalletSummary'


function App() {
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [wallet, setWallet] = useState(null)

  useEffect(() => {
    loadWalletData()
  }, [])

function loadWalletData() {
  setLoading(true)
  setError('')

  Promise.all([
    getWallet(1),
    getWalletTransactions(1),
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
  depositToWallet(1, amount)
    .then(() => {
      loadWalletData()
    })
    .catch(() => {
      setError('Could not deposit money.')
    })
}

function handleTransfer(receiverWalletId, amount) {
  transferMoney(1, receiverWalletId, amount)
    .then(() => {
      loadWalletData()
    })
    .catch(() => {
      setError('Could not transfer money.')
    })
}

  return (
  <main className="dashboard">
    <header className="topbar">
      <div>
        <p className="eyebrow">Digital Wallet System</p>
        <h1>Wallet dashboard</h1>
      </div>

      <span className="wallet-pill">Wallet #1</span>
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