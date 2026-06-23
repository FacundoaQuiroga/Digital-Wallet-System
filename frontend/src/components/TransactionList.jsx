function TransactionList({ transactions }) {
  if (transactions.length === 0) {
    return <p className="muted">No transactions found.</p>
  }

  return (
    <div className="transaction-list">
      {transactions.map((transaction) => (
        <div className="transaction-item" key={transaction.id}>
          <span className={`badge ${transaction.type.toLowerCase()}`}>
            {transaction.type.replace('_', ' ')}
          </span>

          <div>
            <strong>{transaction.description}</strong>
            <p>{new Date(transaction.createdAt).toLocaleString()}</p>
          </div>

          <span className="amount">${transaction.amount}</span>
        </div>
      ))}
    </div>
  )
}

export default TransactionList