function TransactionList({ transactions }) {
  if (transactions.length === 0) {
    return <p>No transactions found.</p>
  }

  return (
    <div>
      {transactions.map((transaction) => (
        <div key={transaction.id}>
          <strong>{transaction.type}</strong>
          <p>{transaction.description}</p>
          <span>${transaction.amount}</span>
        </div>
      ))}
    </div>
  )
}

export default TransactionList