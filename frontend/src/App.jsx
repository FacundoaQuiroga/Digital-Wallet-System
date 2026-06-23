import { useEffect, useState } from 'react'
import './App.css'
import TransactionList from './components/TransactionList'
import { depositToWallet,getWallet,getWalletTransactions,transferMoney } from './services/walletApi'
import DepositForm from './components/DepositForm'
import TransferForm from './components/TransferForm'


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
    <>
      <h1>Digital Wallet System</h1>

      {wallet && (
        <section>
          <h2>Available balance</h2>
          <p>${wallet.balance}</p>
          <p>{wallet.userEmail}</p>
        </section>
      )}

      <DepositForm onDeposit={handleDeposit} />
      <TransferForm onTransfer={handleTransfer} />
        
      <section>
        <h2>Wallet #1 transactions</h2>

        {loading && <p>Loading...</p>}

        {error && <p>{error}</p>}

        {!loading && !error && (
          <TransactionList transactions={transactions} />
        )}
      </section>
    </>
  )
}

export default App