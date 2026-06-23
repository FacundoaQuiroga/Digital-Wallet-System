function WalletSummary({ wallet }) {
  if (!wallet) {
    return null
  }

  return (
    <section className="wallet-summary">
      <div className="balance-card">
        <p className="label">Available balance</p>
        <strong>${wallet.balance}</strong>
      </div>

      <div className="owner-card">
        <p className="label">Wallet owner</p>
        <span>{wallet.userEmail}</span>
      </div>
    </section>
  )
}

export default WalletSummary